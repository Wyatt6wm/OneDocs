# OnePlatform接口文档

## 接口规范定义

### 数据格式

JSON

### 响应规范

| 字段    | 名称       | 说明                                                   |
| ------- | ---------- | ------------------------------------------------------ |
| succ    | 处理标志   | true / false，反馈业务层面处理的成功 / 失败。          |
| traceId | 日志跟踪ID | 调试或出错时用来定位日志进行分析。自动组装，无需关注。 |
| mesg    | 响应信息   | 可选填，成功时返回成功信息，失败时返回失败信息。       |
| data    | 响应数据   | 可选填，成功时返回响应数据。也可以承载复杂响应信息。   |

### 文档编写约定

一、对于以下约定的场景，为了精简接口文档篇幅，文档中只列举标题，不再展开接口字段。此时若有`mesg`响应信息要返回前端，则其内容同文档标题。

1. 业务处理成功，无需返回数据，可能返回成功信息。
2. 业务处理失败，无需返回数据，可能返回失败信息。

二、由权限验证产生的响应信息，比如：未登录、权限不足等，不再列举出来。如果接口定义了权限验证的要求，则默认存在对应的响应场景。

## API接口

### 1. oneplatform-system

#### 1.1. CommonController

##### 1.1.1. 获取验证码

- 请求路径：/api/sys/common/getCaptcha
- 请求方法：get
- 请求参数：无
- 权限验证：无
- 响应数据：

###### 获取成功

```json
{
    "succ": true,
    "data": {
        "captchaKey": "验证码KEY",
        "captchaImage": "验证码图片Base64编码"
    }
}
```

#### 1.2. UserController

##### 1.2.1. 注册用户

- 请求路径：/api/sys/user/registry
- 请求方法：post
- 请求参数

| 参数         | 说明      | 备注                                                         |
| ------------ | --------- | ------------------------------------------------------------ |
| username     | 用户名    | 由大写小写英文、数字组成，长度不大于16位。                   |
| password     | 密码      | 由大写小写英文、数字和特殊字符`.~!@#$%^&*_?`组成，长度6位~16位。明文。 |
| captchaKey   | 验证码KEY | 回传/api/sys/common/getCaptcha接口获取的验证码KEY。          |
| captchaInput | 验证码    |                                                              |

- 权限验证：无
- 响应数据：

###### 请求参数错误

###### 用户名格式错误

###### 密码格式错误

###### 验证码格式错误

###### 验证码失效

###### 验证码错误

###### 用户已注册

###### 数据库错误

###### 注册成功

```json
{
    "succ": true,
    "data": {
        "user": {
        	"id": 1,
        	"username": "用户名"
        }
    }
}
```

##### 1.2.2. 登录认证

- 请求路径：/api/sys/user/login
- 请求方法：post
- 请求参数

| 参数         | 说明      | 备注                                                         |
| ------------ | --------- | ------------------------------------------------------------ |
| username     | 用户名    | 由大写小写英文、数字组成，长度不大于16位。                   |
| password     | 密码      | 由大写小写英文、数字和特殊字符`.~!@#$%^&*_?`组成，长度6位~16位。明文。 |
| captchaKey   | 验证码KEY | 回传/api/sys/common/getCaptcha接口获取的验证码KEY。          |
| captchaInput | 验证码    |                                                              |

- 权限验证：无
- 响应数据：

###### 请求参数错误

###### 用户名格式错误

###### 密码格式错误

###### 验证码格式错误

###### 验证码失效

###### 验证码错误

###### 数据库错误

###### 用户名或密码错误

###### 认证成功

```json
{
    "succ": true,
    "data": {
        "token": "d8c6ed7a3fd446e4a477b20d1ce9cda0",
        "tokenExpiredTime": "2024-06-01T09:21:53.102+00:00",
        "roles": [
        	"角色1",
            "角色2"
        ],
        "auths": [
            "权限标识符1",
            "权限标识符2"
        ]
    }
}
```

##### 1.2.3. 退出登录

- 请求路径：/api/sys/user/logout
- 请求方法：get
- 请求参数：无
- 权限验证：无
- 响应数据：

###### 未登录

###### 退出登录成功

##### 1.2.4. 查询用户角色标识

- 请求路径：/api/sys/user/getRoleIdentifiers
- 请求方法：get
- 请求参数：无
- 权限验证：已登录
- 响应数据：

###### 数据库错误

###### 查询角色标识成功

```json
{
	"succ": true,
    "data": {
        "roles": [
        	"角色1",
            "角色2"
        ] 
    }
}
```

##### 1.2.5. 查询用户权限标识

- 请求路径：/api/sys/user/getAuthIdentifiers
- 请求方法：get
- 请求参数：无
- 权限验证：已登录
- 响应数据：

###### 数据库错误

###### 查询角色标识成功

```json
{
	"succ": true,
    "data": {
        "auths": [
        	"权限1",
            "权限2"
        ] 
    }
}
```

##### 1.2.6. 获取用户信息

- 请求路径：/api/sys/user/getProfile
- 请求方法：get
- 请求参数：无
- 权限验证：已登录
- 响应数据：

###### 获取成功

```json
{
    "succ": true,
    "data": {
        "profile": {
            "userId": 1,
            "username": "用户名",
            "nickname": "昵称",
            "motto": "格言（座右铭）",
            "avatar": "头像地址"
        }
    }
}
```

##### 1.2.7. 修改用户信息

- 请求路径：/api/sys/user/editProfile
- 请求方法：post
- 请求参数：

| 参数     | 说明           | 备注                 |
| -------- | -------------- | -------------------- |
| id       | 用户ID         | 必填                 |
| nickname | 昵称           | 选填。若空则不修改。 |
| motto    | 座右铭（格言） | 选填。若空则不修改。 |

- 权限验证：已登录
- 响应数据：

###### 请求参数错误

###### 数据库错误

###### 昵称已被占用

###### 修改用户信息失败

###### 修改用户信息成功

```json
{
    "succ": true,
    "data": {
        "profile": {
            "id": 1,
            "username": "用户名",
            "nickname": "昵称",
            "motto": "座右铭",
            "avatar": "头像路径"
        }
    }
}
```

##### 1.2.8. 获取用户管理列表

- 请求路径：/api/sys/user/getUserManageList
- 请求方法：post
- 请求参数：无
- 权限验证：已登录，超级管理员角色、管理员角色
- 响应数据：

###### 数据库错误

###### 获取用户管理列表成功

```json
{
    "succ": true,
    "data": {
        "userManageList": [
            {
                "user": {
                    "id": 1,
                    "username": "username",
                    "nickname": "nickname",
                    "motto": "mottto",
                    "avatar": "avatar"
                },
                "roles": [
                    {
                        "id": 1,
                        "identifier": "角色标识符",
                        "name": "角色名称",
                        "description": "角色描述",
                        "activated": 1
                    }, 
                    {
                        "id": 2,
                        "identifier": "角色标识符2",
                        "name": "角色名称",
                        "description": "角色描述",
                        "activated": 0
                    }
                ]
            },
            {
                "user": {
                    "id": 2,
                    "username": "username2",
                    "nickname": "nickname2",
                    "motto": "mottto2",
                    "avatar": "avatar2"
                },
                "roles": []
            }
        ]
    }
}
```

##### 1.2.9. 获取用户所有角色

- 请求路径：/api/sys/user/getRolesOfUser
- 请求方法：post
- 请求参数：

| 参数 | 说明   | 备注   |
| ---- | ------ | ------ |
| id   | 用户ID | 必填。 |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 数据库错误

###### 获取角色所有的权限成功

```json
{
    "succ": true,
    "data": {
        "roles": [
            {
            	"id": 1,
                "identifier": "角色标识符",
                "name": "角色名称",
                "description": "角色描述",
                "activated": 1
            }, 
            {
                "id": 2,
                "identifier": "角色标识符2",
                "name": "角色名称",
                "description": "角色描述",
                "activated": 0
            }
        ]
    }
}
```

##### 1.2.10. 变更角色绑定

- 请求路径：/api/sys/user/changeBinds
- 请求方法：get
- 请求参数：

| 参数       | 说明                 | 备注 |
| ---------- | -------------------- | ---- |
| userId     | 用户ID               |      |
| bindList   | 待绑定角色ID列表     |      |
| unbindList | 待解除绑定角色ID列表 |      |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 变更角色绑定成功

```json
{
    "succ": true,
    "data" : {
        "failBind" : [1, 2, 3],
        "failUnbind": [4, 5, 6]
    }
}
```

##### 1.2.11. 修改密码

- 请求路径：/api/sys/user/changePassword
- 请求方法：post
- 请求参数：

| 参数         | 说明      | 备注                                                |
| ------------ | --------- | --------------------------------------------------- |
| userId       | 用户ID    |                                                     |
| password     | 旧密码    | 格式同登录接口。                                    |
| newPassword  | 新密码    | 格式同登录接口。                                    |
| captchaKey   | 验证码KEY | 回传/api/sys/common/getCaptcha接口获取的验证码KEY。 |
| captchaInput | 验证码    |                                                     |

- 权限验证：已登录
- 响应数据：

###### 请求参数错误

###### 密码格式错误

###### 新密码格式错误

###### 验证码格式错误

###### 修改密码失败

###### 修改密码成功

#### 1.3. RoleController

##### 1.3.1. 新增角色

- 请求路径：/api/sys/role/addRole
- 请求方法：post
- 请求参数：

| 参数        | 说明       | 备注                 |
| ----------- | ---------- | -------------------- |
| identifier  | 角色标识符 | 必填。               |
| name        | 角色名称   | 选填。若空则不修改。 |
| description | 角色描述   | 选填。若空则不修改。 |
| activated   | 激活标志   | 选填。若空则不修改。 |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 数据库错误

###### 角色标识符重复

###### 新增角色成功

```json
{
    "succ": true,
    "data": {
        "role": {
            "id": 1,
            "identifier": "权限标识符",
            "name": "权限名称",
            "description": "权限描述",
            "activated": true
        }
    }
}
```

##### 1.3.2. 变更角色授权

- 请求路径：/api/sys/role/changeGrants
- 请求方法：post
- 请求参数：

| 参数        | 说明                 | 备注 |
| ----------- | -------------------- | ---- |
| roleId      | 角色ID               |      |
| grantList   | 待授权权限ID列表     |      |
| ungrantList | 待解除授权权限ID列表 |      |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 变更角色权限成功

```json
{
    "succ": true,
    "data" : {
        "failGrant" : [1, 2, 3],
        "failUngrant": [4, 5, 6]
    }
}
```

##### 1.3.3. 删除角色

- 请求路径：/api/sys/role/removeRole
- 请求方法：get
- 请求参数：

| 参数   | 说明   | 备注   |
| ------ | ------ | ------ |
| roleId | 角色ID | 必填。 |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 数据库错误

###### 该角色数据不存在

###### 删除角色成功

##### 1.3.4. 编辑角色

- 请求路径：/api/sys/role/editRole
- 请求方法：post
- 请求参数：

| 参数        | 说明       | 备注                 |
| ----------- | ---------- | -------------------- |
| roleId      | 角色ID     | 必填。               |
| identifier  | 角色标识符 | 选填。若空则不修改。 |
| name        | 角色名称   | 选填。若空则不修改。 |
| description | 角色描述   | 选填。若空则不修改。 |
| activated   | 激活标志   | 选填。若空则不修改。 |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 数据库错误

###### 角色ID错误

###### 角色标识符重复

###### 该角色数据不存在

###### 编辑角色成功

```json
{
    "succ": true,
    "data": {
        "role": {
            "id": 1,
            "identifier": "角色标识符",
            "name": "角色名称",
            "description": "角色描述",
            "activated": true
        }
    }
}
```

##### 1.3.5. 获取角色管理列表

- 请求路径：/api/sys/role/getRoleManageList
- 请求方法：get
- 请求参数：无
- 权限验证：已登录，超级管理员角色 / 管理员角色
- 响应数据：

###### 数据库错误

###### 获取角色管理列表成功

```json
{
    "succ": true,
    "data": {
        "roleManageList": [
            {
                "id": 1,
                "identifier": "角色标识",
                "name": "角色名称",
                "description": "角色说明",
                "activated": true
            },
            {
                "id": 2,
                "identifier": "角色标识",
                "name": "角色名称",
                "description": "角色说明",
                "activated": true
            }
        ]
    }
}
```

##### 1.3.6. 获取角色所有权限

- 请求路径：/api/sys/role/getAuthsOfRole
- 请求方法：get
- 请求参数：

| 参数   | 说明   | 备注   |
| ------ | ------ | ------ |
| roleId | 角色ID | 必填。 |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 数据库错误

###### 获取角色所有的权限成功

```json
{
    "succ": true,
    "data": {
        "auths": [
            {
                "id": 1,
                "identifier": "view:xxxxx",
                "name": "XXXXX权限",
                "description": "权限说明",
                "activated": true
            },
            {
                "id": 2,
                "identifier": "api:xxxxx",
                "name": "XXXXX权限",
                "description": "权限说明",
                "activated": false
            }
        ]
    }
}
```

#### 1.4. AuthController

##### 1.4.1. 新增权限

- 请求路径：/api/sys/auth/addAuth
- 请求方法：post
- 请求参数：

| 参数        | 说明       | 备注                 |
| ----------- | ---------- | -------------------- |
| identifier  | 权限标识符 | 必填。               |
| name        | 权限名称   | 选填。若空则不修改。 |
| description | 权限描述   | 选填。若空则不修改。 |
| activated   | 激活标志   | 选填。若空则不修改。 |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 数据库错误

###### 权限标识符重复

###### 新增权限成功

```json
{
    "succ": true,
    "data": {
        "auth": {
            "id": 1,
            "identifier": "权限标识符",
            "name": "权限名称",
            "description": "权限描述",
            "activated": true
        }
    }
}
```

##### 1.4.2. 删除权限

- 请求路径：/api/sys/auth/removeAuth
- 请求方法：get
- 请求参数：

| 参数   | 说明   | 备注   |
| ------ | ------ | ------ |
| authId | 权限ID | 必填。 |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 数据库错误

###### 该权限数据不存在

###### 删除权限成功

##### 1.4.3. 编辑权限

- 请求路径：/api/sys/auth/editAuth
- 请求方法：post
- 请求参数：

| 参数        | 说明       | 备注                 |
| ----------- | ---------- | -------------------- |
| id          | 权限ID     | 必填。               |
| identifier  | 权限标识符 | 选填。若空则不修改。 |
| name        | 权限名称   | 选填。若空则不修改。 |
| description | 权限描述   | 选填。若空则不修改。 |
| activated   | 激活标志   | 选填。若空则不修改。 |

- 权限验证：已登录，超级管理员角色
- 响应数据：

###### 请求参数错误

###### 数据库错误

###### 权限ID错误

###### 权限标识符重复

###### 该权限数据不存在

###### 编辑权限成功

```json
{
    "succ": true,
    "data": {
        "auth": {
            "id": 1,
            "identifier": "权限标识符",
            "name": "权限名称",
            "description": "权限描述",
            "activated": true
        }
    }
}
```

##### 1.4.4. 获取权限管理列表

- 请求路径：/api/sys/auth/getAuthManageList
- 请求方法：get
- 请求参数：无
- 权限验证：已登录，超级管理员角色 / 管理员角色
- 响应数据：

###### 数据库错误

###### 获取权限管理列表成功

```json
{
    "succ": true,
    "data": {
        "authManageList": [
            {
                "id": 1,
                "identifier": "view:xxxxx",
                "name": "XXXXX权限",
                "description": "权限说明",
                "activated": true
            },
            {
                "id": 2,
                "identifier": "api:xxxxx",
                "name": "XXXXX权限",
                "description": "权限说明",
                "activated": false
            }
        ]
    }
}
```

### 2. oneplatform-todo

待办状态定义：

- draft：草稿
- submit：已提交(未开始)
- progress：进行中
- edit：进行转变更
- finish：已完成
- cancel：已取消

#### 2.1. TodoController

##### 2.1.1. 保存草稿/保存变更

- 请求路径：/api/todo/saveDraft
- 请求方法：post
- 请求参数：

| 参数         | 说明           | 备注                                   |
| ------------ | -------------- | -------------------------------------- |
| id           | 待办ID         | 为null表示新增草稿，非空表示保存草稿。 |
| category     | 分类           | work：工作待办，daily：日常待办        |
| name         | 待办名称       |                                        |
| emergency    | 紧急度         | true / false                           |
| importance   | 重要性         | true / false                           |
| deadline     | 截止日期       |                                        |
| detail       | 待办详情       |                                        |
| workload     | 工作量评估     |                                        |
| workloadHour | 工作量（小时） |                                        |
| workloadDay  | 工作量（天）   |                                        |
| status       | 状态           | 待办的当前状态                         |

- 权限验证：已登录
- 响应数据：

###### 保存草稿成功

```json
{
    "succ": true,
    "data": {
        "todo": {
            "id": 1,
            "category": "work",
            "name": "待办名称",
            "emergency": true,
            "importance": true,
            "deadline": "2023-07-18 12:00:00",
            "detail": "待办详情",
            "workload": "工作量评估",
            "workloadHour": 1.1,
            "workloadDay": 2,
            "status": "draft"
        }
    }
}
```

##### 2.1.2. 提交待办/确认变更

- 请求路径：/api/todo/submitTodo
- 请求方法：post
- 请求参数：

| 参数         | 说明           | 备注                                   |
| ------------ | -------------- | -------------------------------------- |
| id           | 待办ID         | 为null表示新增草稿，非空表示保存草稿。 |
| category     | 分类           | work：工作待办，daily：日常待办        |
| name         | 待办名称       |                                        |
| emergency    | 紧急度         | true / false                           |
| importance   | 重要性         | true / false                           |
| deadline     | 截止日期       |                                        |
| detail       | 待办详情       |                                        |
| workload     | 工作量评估     |                                        |
| workloadHour | 工作量（小时） |                                        |
| workloadDay  | 工作量（天）   |                                        |
| status       | 状态           | 待办的当前状态                         |

- 权限验证：已登录
- 响应数据：

###### 提交待办成功

```json
{
    "succ": true,
    "data": {
        "todo": {
            "id": 1,
            "category": "work",
            "name": "待办名称",
            "emergency": true,
            "importance": true,
            "deadline": "2023-07-18 12:00:00",
            "detail": "待办详情",
            "workload": "工作量评估",
            "workloadHour": 1.1,
            "workloadDay": 2,
            "submitTime": "2023-07-18 12:00:00",
            "status": "submit",
            "lastLogId": 1
        },
        "todoLogList": [
            {
            	"id": 1,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	},
            {
            	"id": 2,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	}
        ]
    }
}
```

##### 2.1.3. 删除草稿

- 请求路径：/api/todo/removeDraft
- 请求方法：get
- 请求参数：

| 参数   | 说明             | 备注   |
| ------ | ---------------- | ------ |
| todoId | 待删除草稿待办ID | 必填。 |

- 权限验证：已登录
- 响应数据：

###### 成功删除草稿

##### 2.1.4. 待办切换到开始状态

- 请求路径：/api/todo/toProgress
- 请求方法：get
- 请求参数：

| 参数   | 说明   | 备注   |
| ------ | ------ | ------ |
| todoId | 待办ID | 必填。 |

- 权限验证：已登录
- 响应数据：

###### 成功切换到开始状态

```json
{
    "succ": true,
    "data": {
        "todo": {
            "beginTime": "2023-07-18 12:00:00",
            "status": "draft",
            "lastLogId": 1
        },
        "todoLogList": [
            {
            	"id": 1,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	},
            {
            	"id": 2,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	}
        ]
    }
}
```

##### 2.1.5. 待办切换到变更状态

- 请求路径：/api/todo/toEdit
- 请求方法：get
- 请求参数：

| 参数   | 说明   | 备注   |
| ------ | ------ | ------ |
| todoId | 待办ID | 必填。 |

- 权限验证：已登录
- 响应数据：

###### 成功切换到变更状态

```json
{
    "succ": true,
    "data": {
        "todo": {
            "status": "edit",
            "lastLogId": 1
        },
        "todoLogList": [
            {
            	"id": 1,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	},
            {
            	"id": 2,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	}
        ]
    }
}
```

##### 2.1.6. 完成待办

- 请求路径：/api/todo/finishTodo
- 请求方法：post
- 请求参数：

| 参数       | 说明     | 备注   |
| ---------- | -------- | ------ |
| todoId     | 待办ID   | 必填。 |
| conslusion | 完成结论 |        |

- 权限验证：已登录
- 响应数据：

###### 成功完成待办

```json
{
    "succ": true,
    "data": {
        "todo": {
            "finishTime": "2023-07-18 12:00:00",
            "status": "finish",
            "conclusion": "完成结论"
            "lastLogId": 1
        },
        "todoLogList": [
            {
            	"id": 1,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	},
            {
            	"id": 2,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	}
        ]
    }
}
```

##### 2.1.7. 取消待办

- 请求路径：/api/todo/cancelTodo
- 请求方法：post
- 请求参数：

| 参数       | 说明     | 备注   |
| ---------- | -------- | ------ |
| todoId     | 待办ID   | 必填。 |
| conslusion | 取消原因 |        |

- 权限验证：已登录
- 响应数据：

###### 成功取消待办

```json
{
    "succ": true,
    "data": {
        "todo": {
            "status": "cancel",
            "conclusion": "取消原因"
            "lastLogId": 1
        },
        "todoLogList": [
            {
            	"id": 1,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	},
            {
            	"id": 2,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	}
        ]
    }
}
```

##### 2.1.8. 获取待办列表

- 请求路径：/api/todo/getTodoList
- 请求方法：get
- 请求参数：

| 参数     | 说明 | 备注   |
| -------- | ---- | ------ |
| category | 分类 | 必填。 |

- 权限验证：已登录
- 响应数据：

###### 成功获取待办列表

```json
{
    "succ": true,
    "data": {
        "todoList": [
            {
            	"id": 1,
                "tenant": 1,
            	"category": "work",
                "name": "待办名称",
                "emergency": true,
            	"importance": true,
            	"deadline": "2023-07-18 12:00:00",
            	"detail": "待办详情",
            	"workload": "工作量评估",
            	"workloadHour": 1.1,
            	"workloadDay": 2,
                "submitTime": "2023-07-18 12:00:00",
                "beginTime": "2023-07-18 12:00:00",
                "finishTime": "2023-07-18 12:00:00",
            	"status": "draft",
                "conclusion": "完成结论/取消原因",
                "lastLogId": 1
        	}
        ],
        "lastLogList": [
            {
            	"id": 1,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	},
            {
            	"id": 2,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	}
        ]
    }
}
```

##### 2.1.9. 查询待办

- 请求路径：/api/todo/getTodo
- 请求方法：get
- 请求参数：

| 参数   | 说明   | 备注   |
| ------ | ------ | ------ |
| todoId | 待办ID | 必填。 |

- 权限验证：已登录
- 响应数据：

###### 成功获取待办列表

```json
{
    "succ": true,
    "data": {
        "todo": {
            "id": 1,
            "tenant": 1,
            "category": "work",
            "name": "待办名称",
            "emergency": true,
            "importance": true,
            "deadline": "2023-07-18 12:00:00",
            "detail": "待办详情",
            "workload": "工作量评估",
            "workloadHour": 1.1,
            "workloadDay": 2,
            "submitTime": "2023-07-18 12:00:00",
            "beginTime": "2023-07-18 12:00:00",
            "finishTime": "2023-07-18 12:00:00",
            "status": "draft",
            "conclusion": "完成结论/取消原因",
            "lastLogId": 1
        }
    }
}
```

#### 2.2. TodoLogController

##### 2.2.1. 新增进度跟踪记录

- 请求路径：/api/todo/log/addTodoLog
- 请求方法：post
- 请求参数：

| 参数   | 说明   | 备注   |
| ------ | ------ | ------ |
| todoId | 待办ID | 必填。 |
| title  | 标题   | 必填。 |
| log    | 内容   |        |

- 权限验证：已登录
- 响应数据：

###### 成功新增进度跟踪记录

```json
{
    "succ": true,
    "data": {
        "todoLogList": [
            {
            	"id": 1,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	},
            {
            	"id": 2,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	}
        ]
    }
}
```

##### 2.2.2. 获取待办日志列表

- 请求路径：/api/todo/log/getTodoLogList
- 请求方法：get
- 请求参数：

| 参数   | 说明   | 备注   |
| ------ | ------ | ------ |
| todoId | 待办ID | 必填。 |

- 权限验证：已登录
- 响应数据：

###### 成功获取待办日志列表

```json
{
    "succ": true,
    "data": {
        "todoLogList": [
            {
            	"id": 1,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	},
            {
            	"id": 2,
            	"tenant": 1,
            	"todoId": 1,
            	"title": "待办进度标题",
            	"log": "待办进度内容",
            	"submitTime": "2023-07-18 12:00:00"
        	}
        ]
    }
}
```

