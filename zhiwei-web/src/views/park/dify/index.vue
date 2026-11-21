<template>
  <div class="dify-chat">
    <!-- 左侧会话列表 -->
    <aside class="history-panel">
      <button class="new-chat-btn" :disabled="isStreaming" @click="addChat">+ 新增对话</button>
      <div class="history-title">历史记录</div>
      <div class="history-list">
        <div
          v-for="item in conversations"
          :key="item.id"
          class="history-item"
          :class="{ active: item.id === currentConversationId }"
          @click="selectConversation(item)"
        >
          <span class="history-name" :title="item.name">{{ item.name || '新会话' }}</span>
          <span class="delete-btn" title="删除会话" @click.stop="handleDelete(item)">
            <svg width="14" height="14" viewBox="0 0 1024 1024" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M320 896c0 35.2 28.8 64 64 64h256c35.2 0 64-28.8 64-64V320H320v576zm576-704h-192l-32-64H352l-32 64H128v64h768v-64z" fill="#f56c6c"/>
            </svg>
          </span>
        </div>
        <div v-if="conversations.length === 0" class="history-empty">暂无历史会话</div>
      </div>
    </aside>

    <!-- 右侧聊天区域 -->
    <section class="chat-main">
      <div class="messages" ref="messagesRef">
        <div v-if="messages.length === 0" class="empty-state">
          <img src="@/assets/logo/ai_log.png" class="ai-logo" alt="AI" />
          <div class="empty-title">您好！我是 Dify 智能助手</div>
          <div class="empty-desc">请向我提问，我会尽力为您解答。</div>
        </div>

        <div
          v-for="(message, index) in messages"
          :key="index"
          :class="['message', message.role]"
        >
          <img v-if="message.role === 'assistant'" src="@/assets/logo/ai_log.png" class="ai-logo" alt="AI" />
          <div v-if="message.role === 'user'" class="content">{{ message.content }}</div>
          <div v-else class="content">
            <div v-if="message.thinking" class="think-box">
              <div class="think-head" @click="toggleThink(index)">
                <span>🤔 思考过程</span>
                <span class="think-status">{{ message.thinkingDone ? '已完成' : '思考中...' }}</span>
                <span class="think-arrow">{{ message.thinkOpen ? '收起 ▲' : '展开 ▼' }}</span>
              </div>
              <div v-show="message.thinkOpen" class="think-body">{{ message.thinking }}</div>
            </div>
            <div v-if="message.content" class="markdown-body" v-html="renderMarkdown(message.content)"></div>
            <span v-else-if="isStreaming && !message.thinking && index === messages.length - 1" class="thinking">正在思考中...</span>
          </div>
        </div>
      </div>

      <div class="input-area">
        <div class="input-box">
          <textarea
            ref="inputRef"
            v-model="userInput"
            rows="1"
            placeholder="请输入您的问题..."
            @keydown="handleKeydown"
          ></textarea>
          <button class="send-btn" :disabled="isStreaming || !userInput.trim()" @click="sendMessage">
            发送
          </button>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { marked } from 'marked'
import useUserStore from '@/store/modules/user'
import {
  sendDifyChat,
  getDifyConversations,
  getDifyMessages,
  deleteDifyConversation
} from '@/api/park/dify'

marked.setOptions({ breaks: true, gfm: true })

const userStore = useUserStore()
const user = userStore.id ? String(userStore.id) : 'zhiwei-user'

const messagesRef = ref(null)
const inputRef = ref(null)
const userInput = ref('')
const isStreaming = ref(false)
const messages = ref([])
const conversations = ref([])
// 当前会话ID，null 表示新会话（由 Dify 在首条消息时生成）
const currentConversationId = ref(null)

/** 处理会话列表数据 */
const mapConversation = (item) => ({
  id: item.id,
  name: item.name || '新会话',
  updatedAt: item.updated_at
})

/** 加载会话列表 */
const loadConversations = async (autoSelect = false) => {
  try {
    const res = await getDifyConversations({ user, limit: 100 })
    const list = res.data?.data || []
    conversations.value = list.map(mapConversation)
    console.log('[Dify-AI] 会话列表加载成功，数量:', conversations.value.length)
    if (autoSelect && !currentConversationId.value && conversations.value.length > 0) {
      await selectConversation(conversations.value[0])
    }
  } catch (e) {
    console.error('[Dify-AI] 加载会话列表失败:', e)
    ElMessage.error('加载会话列表失败')
  }
}

/** 加载指定会话的历史消息 */
const loadMessages = async (conversationId) => {
  try {
    const res = await getDifyMessages({ conversationId, user, limit: 100 })
    const list = res.data?.data || []
    console.log('[Dify-AI] 历史消息数量:', list.length)
    // Dify 返回的消息为倒序，这里按时间正序展示
    const sorted = [...list].sort((a, b) => (a.created_at || 0) - (b.created_at || 0))
    const result = []
    sorted.forEach((item) => {
      if (item.query) {
        result.push({ role: 'user', content: item.query })
      }
      if (item.answer) {
        const parsed = parseAnswer(item.answer)
        result.push({
          role: 'assistant',
          content: parsed.content,
          thinking: parsed.thinking,
          thinkingDone: true,
          thinkOpen: false
        })
      }
    })
    messages.value = result
    await scrollToBottom()
  } catch (e) {
    console.error('[Dify-AI] 加载历史消息失败:', e)
    ElMessage.error('加载历史消息失败')
  }
}

/** 切换会话 */
const selectConversation = async (item) => {
  if (isStreaming.value) {
    ElMessage.warning('正在生成回答，请稍候')
    return
  }
  console.log('[Dify-AI] 切换会话:', item.id, item.name)
  currentConversationId.value = item.id
  messages.value = []
  await loadMessages(item.id)
}

/** 新增对话 */
const addChat = () => {
  if (isStreaming.value) {
    ElMessage.warning('正在生成回答，请稍候')
    return
  }
  console.log('[Dify-AI] 新增对话')
  currentConversationId.value = null
  messages.value = []
  userInput.value = ''
  inputRef.value?.focus()
}

/** 滚动到底部 */
const scrollToBottom = async () => {
  await nextTick()
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
}

/** 从 SSE 事件块中提取 data 内容 */
const extractSseData = (block) => {
  const dataLines = []
  block.split(/\r?\n/).forEach((line) => {
    if (line.startsWith('data:')) {
      dataLines.push(line.slice(5).trimStart())
    }
  })
  return dataLines.join('\n').trim()
}

/** 拆分模型思考内容（<think>...</think>）和最终回答 */
const parseAnswer = (raw) => {
  if (!raw) {
    return { thinking: '', content: '', thinkingDone: false }
  }
  const thinkRegex = /<think(?:ing)?>([\s\S]*?)(?:<\/think(?:ing)?>|$)/i
  const match = raw.match(thinkRegex)
  if (!match) {
    return { thinking: '', content: raw, thinkingDone: true }
  }
  return {
    thinking: match[1],
    content: raw.replace(thinkRegex, '').trim(),
    thinkingDone: /<\/think(?:ing)?>/i.test(raw)
  }
}

/** 展开/收起思考过程 */
const toggleThink = (index) => {
  const message = messages.value[index]
  if (message) {
    message.thinkOpen = !message.thinkOpen
  }
}

/** 发送消息 */
const sendMessage = async () => {
  if (isStreaming.value || !userInput.value.trim()) return
  const query = userInput.value.trim()
  userInput.value = ''
  messages.value.push({ role: 'user', content: query })
  messages.value.push({
    role: 'assistant',
    content: '',
    thinking: '',
    thinkingDone: false,
    thinkOpen: true
  })
  const assistantIndex = messages.value.length - 1
  isStreaming.value = true
  await scrollToBottom()

  let conversationId = currentConversationId.value
  let answerText = ''

  try {
    console.log('[Dify-AI] 开始发送对话, conversationId:', conversationId)
    const response = await sendDifyChat({ query, conversationId, user })
    console.log('[Dify-AI] 对话接口响应状态:', response.status,
      'content-type:', response.headers.get('content-type'))
    if (!response.ok) {
      throw new Error('对话接口请求失败: HTTP ' + response.status)
    }
    if (!response.body) {
      throw new Error('当前浏览器不支持流式响应')
    }

    const reader = response.body.getReader()
    const decoder = new TextDecoder('utf-8')
    let buffer = ''

    const handleBlock = (block) => {
      const data = extractSseData(block)
      if (!data || data === '[DONE]') return
      let payload
      try {
        payload = JSON.parse(data)
      } catch (e) {
        console.warn('[Dify-AI] SSE数据解析失败:', data)
        return
      }
      console.debug('[Dify-AI] 收到SSE数据:', payload)
      // 新会话时由 Dify 返回 conversation_id，后续请求必须带上
      if (payload.conversation_id && !conversationId) {
        conversationId = payload.conversation_id
        currentConversationId.value = conversationId
        console.log('[Dify-AI] 获取到新的会话ID:', conversationId)
      }
      if (payload.event === 'error') {
        throw new Error(payload.message || 'Dify 返回错误')
      }
      if (typeof payload.answer === 'string' && payload.answer) {
        answerText += payload.answer
        const parsed = parseAnswer(answerText)
        const assistantMessage = messages.value[assistantIndex]
        assistantMessage.thinking = parsed.thinking
        assistantMessage.content = parsed.content
        // 思考结束（出现 </think>）后自动收起思考框，开始展示最终回答
        if (parsed.thinkingDone && !assistantMessage.thinkingDone) {
          assistantMessage.thinkingDone = true
          assistantMessage.thinkOpen = false
        }
        scrollToBottom()
      }
    }

    while (true) {
      const { value, done } = await reader.read()
      if (done) break
      buffer += decoder.decode(value, { stream: true })
      const blocks = buffer.split(/\r?\n\r?\n/)
      buffer = blocks.pop() || ''
      for (const block of blocks) {
        handleBlock(block)
      }
    }
    // 处理最后一段可能没有以空行结尾的数据
    buffer += decoder.decode()
    if (buffer.trim()) {
      handleBlock(buffer)
    }
    console.log('[Dify-AI] 流式输出结束，回答长度:', answerText.length)
  } catch (e) {
    console.error('[Dify-AI] 对话异常:', e)
    if (!messages.value[assistantIndex]?.content) {
      messages.value[assistantIndex].content = '抱歉，发生了错误，请稍后重试。'
    }
    ElMessage.error(e.message || '对话失败，请稍后重试')
  } finally {
    isStreaming.value = false
    await scrollToBottom()
    // 刷新会话列表，获取 Dify 自动生成的会话名称
    await loadConversations(false)
  }
}

/** 删除会话 */
const handleDelete = async (item) => {
  if (isStreaming.value) {
    ElMessage.warning('正在生成回答，请稍候')
    return
  }
  try {
    await ElMessageBox.confirm(`确定删除会话“${item.name || '新会话'}”吗？`, '删除确认', {
      confirmButtonText: '删除',
      cancelButtonText: '取消',
      type: 'warning'
    })
  } catch (e) {
    return
  }
  try {
    await deleteDifyConversation(item.id, user)
    ElMessage.success('删除成功')
    console.log('[Dify-AI] 会话删除成功:', item.id)
    if (currentConversationId.value === item.id) {
      currentConversationId.value = null
      messages.value = []
    }
    await loadConversations(false)
    if (!currentConversationId.value && conversations.value.length > 0) {
      await selectConversation(conversations.value[0])
    }
  } catch (e) {
    console.error('[Dify-AI] 删除会话失败:', e)
    ElMessage.error('删除会话失败')
  }
}

/** 回车发送，Shift+回车换行 */
const handleKeydown = (e) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}

/** Markdown 渲染 */
const renderMarkdown = (text) => {
  return marked.parse(text || '')
}

onMounted(async () => {
  console.log('[Dify-AI] 页面初始化，当前用户标识:', user)
  await loadConversations(true)
})
</script>

<style scoped>
.dify-chat {
  display: flex;
  height: calc(100vh - 84px);
  background: #f5f6fa;
  overflow: hidden;
}

/* 左侧历史记录 */
.history-panel {
  width: 220px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  background: #f7f8fa;
  border-right: 1px solid #e8e8e8;
  padding: 16px 12px;
  box-sizing: border-box;
}

.new-chat-btn {
  width: 100%;
  height: 40px;
  border: none;
  border-radius: 6px;
  background: #2f7cf6;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.2s;
}

.new-chat-btn:hover:not(:disabled) {
  background: #1f6ae0;
}

.new-chat-btn:disabled {
  background: #a8c6f5;
  cursor: not-allowed;
}

.history-title {
  margin: 20px 6px 10px;
  font-size: 14px;
  font-weight: 600;
  color: #333;
}

.history-list {
  flex: 1;
  overflow-y: auto;
}

.history-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
  margin-bottom: 4px;
  border-radius: 6px;
  font-size: 14px;
  color: #555;
  cursor: pointer;
  transition: background 0.2s;
}

.history-item:hover {
  background: #eef2f8;
}

.history-item.active {
  background: #e8f0fe;
  color: #2f7cf6;
  font-weight: 500;
}

.history-name {
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.delete-btn {
  display: none;
  align-items: center;
  justify-content: center;
  width: 16px;
  height: 16px;
  margin-left: 8px;
}

.history-item:hover .delete-btn {
  display: flex;
}

.history-empty {
  padding: 12px;
  font-size: 13px;
  color: #999;
  text-align: center;
}

/* 右侧聊天区域 */
.chat-main {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  background: #fff;
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 28px 40px;
  box-sizing: border-box;
}

.message {
  width: 100%;
  max-width: 900px;
  margin: 0 auto 22px;
  display: flex;
}

.message.user {
  justify-content: flex-end;
}

.message.user .content {
  max-width: 75%;
  padding: 10px 16px;
  border-radius: 12px 2px 12px 12px;
  background: #e8f0fe;
  color: #222;
  font-size: 15px;
  line-height: 1.7;
  white-space: pre-wrap;
  word-break: break-word;
}

.message.assistant {
  align-items: flex-start;
}

.ai-logo {
  width: 32px;
  height: 32px;
  flex-shrink: 0;
  margin-right: 12px;
  border-radius: 8px;
}

.message.assistant .content {
  flex: 1;
  min-width: 0;
  color: #222;
  font-size: 15px;
  line-height: 1.8;
  word-break: break-word;
}

.thinking {
  color: #999;
  font-size: 14px;
}

.empty-state {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.empty-state .ai-logo {
  width: 56px;
  height: 56px;
  margin-right: 0;
  margin-bottom: 16px;
  border-radius: 12px;
}

.empty-title {
  margin-bottom: 8px;
  font-size: 18px;
  font-weight: 600;
  color: #333;
}

.empty-desc {
  font-size: 14px;
  color: #999;
}

/* 输入区域 */
.input-area {
  padding: 14px 40px 18px;
  border-top: 1px solid #f0f0f0;
  background: #fff;
}

.input-box {
  display: flex;
  align-items: flex-end;
  max-width: 900px;
  margin: 0 auto;
  padding: 8px 10px;
  border: 1px solid #e2e5ea;
  border-radius: 8px;
  background: #fff;
  transition: border-color 0.2s;
}

.input-box:focus-within {
  border-color: #2f7cf6;
}

.input-box textarea {
  flex: 1;
  min-height: 24px;
  max-height: 120px;
  padding: 6px 8px;
  border: none;
  outline: none;
  resize: none;
  background: transparent;
  font-family: inherit;
  font-size: 15px;
  line-height: 24px;
}

.send-btn {
  min-width: 72px;
  height: 38px;
  margin-left: 8px;
  border: none;
  border-radius: 6px;
  background: #2f7cf6;
  color: #fff;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.2s;
}

.send-btn:hover:not(:disabled) {
  background: #1f6ae0;
}

.send-btn:disabled {
  background: #c9d6ea;
  cursor: not-allowed;
}

/* Markdown 内容样式 */
/* 思考过程区域 */
.think-box {
  margin-bottom: 10px;
  border: 1px solid #eef0f3;
  border-radius: 8px;
  background: #fafbfc;
  overflow: hidden;
}

.think-head {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  font-size: 13px;
  color: #666;
  cursor: pointer;
  user-select: none;
}

.think-status {
  color: #2f7cf6;
}

.think-arrow {
  margin-left: auto;
  font-size: 12px;
  color: #999;
}

.think-body {
  max-height: 260px;
  overflow-y: auto;
  padding: 8px 12px 10px;
  border-top: 1px dashed #eef0f3;
  font-size: 13px;
  line-height: 1.7;
  color: #888;
  white-space: pre-wrap;
  word-break: break-word;
}

.markdown-body :deep(p) {
  margin: 0 0 12px;
}

.markdown-body :deep(p:last-child) {
  margin-bottom: 0;
}

.markdown-body :deep(ul),
.markdown-body :deep(ol) {
  margin: 0 0 12px;
  padding-left: 22px;
}

.markdown-body :deep(table) {
  width: 100%;
  margin: 12px 0;
  border-collapse: collapse;
}

.markdown-body :deep(th),
.markdown-body :deep(td) {
  padding: 8px 12px;
  border: 1px solid #d0d7de;
  text-align: left;
}

.markdown-body :deep(th) {
  background: #f6f8fa;
  font-weight: 600;
}

.markdown-body :deep(pre) {
  margin: 12px 0;
  padding: 12px 16px;
  border-radius: 6px;
  background: #f6f8fa;
  overflow-x: auto;
}

.markdown-body :deep(code) {
  padding: 2px 5px;
  border-radius: 4px;
  background: #f2f3f5;
  font-family: Consolas, Menlo, monospace;
  font-size: 14px;
}

.markdown-body :deep(pre code) {
  padding: 0;
  background: transparent;
}
</style>
