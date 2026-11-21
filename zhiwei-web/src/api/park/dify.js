import request from '@/utils/request'
import { getToken } from '@/utils/auth'

const DEFAULT_USER = 'zhiwei-user'

/**
 * 发送对话消息（流式输出）
 * 使用 fetch 读取 SSE 流，不能使用 axios
 */
export function sendDifyChat({ query, conversationId, user }) {
  const baseURL = import.meta.env.VITE_APP_BASE_API || ''
  const body = {
    query: query,
    conversationId: conversationId || '',
    user: user || DEFAULT_USER
  }
  console.log('[Dify-AI] 发送对话请求:', body)
  return fetch(baseURL + '/ai/dify/chat', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'text/event-stream',
      ...(getToken() ? { Authorization: 'Bearer ' + getToken() } : {})
    },
    body: JSON.stringify(body)
  })
}

/**
 * 获取会话列表
 */
export function getDifyConversations(params) {
  console.log('[Dify-AI] 获取会话列表:', params)
  return request({
    url: '/ai/dify/conversations',
    method: 'get',
    params: params
  })
}

/**
 * 获取会话历史消息
 */
export function getDifyMessages(params) {
  console.log('[Dify-AI] 获取会话历史消息:', params)
  return request({
    url: '/ai/dify/messages',
    method: 'get',
    params: params
  })
}

/**
 * 删除会话
 */
export function deleteDifyConversation(conversationId, user) {
  console.log('[Dify-AI] 删除会话:', conversationId)
  return request({
    url: '/ai/dify/conversations/' + conversationId,
    method: 'delete',
    params: { user: user }
  })
}
