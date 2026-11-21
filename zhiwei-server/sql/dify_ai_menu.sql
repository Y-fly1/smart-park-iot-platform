-- ----------------------------
-- Dify-AI 智能体对话菜单（增量脚本，可重复执行）
-- 说明：菜单挂在“智能问答”目录（menu_id = 2052）下
-- ----------------------------

-- 先清理可能已存在的同名菜单，避免重复执行产生重复数据
DELETE rm FROM `sys_role_menu` rm
    INNER JOIN `sys_menu` m ON rm.`menu_id` = m.`menu_id`
WHERE m.`menu_name` = 'Dify-AI' AND m.`parent_id` = 2052;
DELETE FROM `sys_menu` WHERE `menu_name` = 'Dify-AI' AND `parent_id` = 2052;

INSERT INTO `sys_menu` (`menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `route_name`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`)
VALUES ('Dify-AI', 2052, 2, 'difyAi', 'park/dify/index', '', 'DifyAi', 1, 0, 'C', '0', '0', '', 'message', 'admin', NOW(), '', NULL, 'Dify智能体对话菜单');

-- 取最新插入的菜单ID，关联给巡检员角色（role_id = 102）
SET @dify_menu_id = LAST_INSERT_ID();
INSERT INTO `sys_role_menu` (`role_id`, `menu_id`) VALUES (102, @dify_menu_id);
