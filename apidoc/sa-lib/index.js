// 声明所有全局内容
var sa_mcontent = {
    // 例如：定义res, 你可以在md文档中使用 import(res)，来导入这一段话
    res: '``` js\n\t{\n\t\t"code": 200,	// 成功时=200, 失败时=500  msg=失败原因\n\t\t"msg": "ok",\n\t\t"data": {}\n\t} \n```',
}

// 声明插件
var sa_plugins = function(hook) {
    // 解析之前执行
    hook.beforeEach(function(content) {
        content = refMd_p2table(content) // 参数转表格
        content = refMd_import2content(content) // 加载import
        content = refMd_api2trim(content) // api去除空格
        content = refMd_var(content) // 更换变量
        return content
    })

}



// 获取一行表格
// 参数名字，类型，默认值，说明
function getTrMd(name, type, default_value, remrak) {
    var str = '\n|' + name + '|' + type + '|' + default_value + '|' + remrak + '|'
    return str
}

// 加工md，将其中的```p 转换为table格式
function refMd_p2table(content) {
    // 1、取出全文中所有的 ```d
    var reg = /```\s*p[\s\S]*?```/gim // [\s\S]*=任意字符n次，?=非贪婪模式
    var pArr = content.match(reg) || [] // 返回所有匹配项数组
        // 遍历并转换
    pArr.forEach(function(p) {
        // 声明表头
        var table =
            '\n' +
            '| 参数名称| 类型| 默认值|说明|\n' +
            '| :--------| :--------| :--------|:--------|'

        // 将这个p内容按换行符切割
        var canStr = p
            .replace(/```\s*p/, '')
            .replace('```', '')
            .trim() // 去除首行尾行
        var canArr = canStr.split('\n') || [] // 按行切割
            // var bodyPList = [];	// 二维数组记录一下参数集合

        // 开始逐行转换为tr
        canArr.forEach(function(canStr) {
            // 去除空格
            canStr = canStr.trim()

            // 声明四大变量的值
            let name = '' // 参数名子
            let type = '' // 数据类型
            let default_value = '' // 默认值
            let remrak = '' // 参数说明

            // 如果带有数据类型
            if (canStr.indexOf('{') > -1 && canStr.indexOf('}') > -1) {
                canStr = canStr.replace('{', '') // 去除掉前{
                let sjArr = canStr.split('}') // 按照}分割
                type = sjArr[0].trim()
                canStr = sjArr[1].trim()
            }

            // 切割成数组
            var canArray = canStr.split(/[\s\t\n]/) || []
                // canArray = arrayTrimSpace(canArray); 	// 去除空格元素
                // 如果开发者写的不规范，使其超过2个元素，则强制改为2个元素
            if (canArray.length == 0) {
                return
            }
            if (canArray.length == 1) {
                canArray.push('')
            }

            // =========== 开始判断 5种情况 ==================
            let one = canArray[0]
            let two = canArray[1]

            // 情况1   id=1  xxxx
            if (one.indexOf('=') > -1 && one.indexOf('=') < one.length - 1) {
                name = one.split('=')[0]
                default_value = one.split('=')[1]
                canArray.splice(0, 1)
            }
            // 情况2   id= 1  xxxx
            else if (one.indexOf('=') == one.length - 1) {
                name = one.split('=')[0]
                default_value = two
                canArray.splice(0, 2)
            }
            // 情况3   id =1  xxxx
            else if (
                one.indexOf('=') == -1 &&
                two.indexOf('=') == 0 &&
                two.length > 1
            ) {
                name = one
                default_value = two.split('=')[1]
                canArray.splice(0, 2)
            }
            // 情况4   id = 1  xxxx
            else if (one.indexOf('=') == -1 && two == '=') {
                name = one
                default_value = canArray[2] || ''
                canArray.splice(0, 3)
            }
            // 情况5 	id 	1	xxxx   或其它
            // else if(one.indexOf('=') == -1 && two.indexOf('=') != 0) {
            else {
                name = one
                canArray.splice(0, 1)
            }

            // 剩下的元素，都拼接remrak
            remrak = canArray.join('')

            // 添加到表格
            table += getTrMd(name, type, default_value, remrak)
                // bodyPList.push([name, type, default_value, remrak]);
        })

        // console.log(p);
        // 将原始p内容替换成为table内容
        table += '\n'
            // table += '\n<div class="body-p-list" style="display: none;">' + JSON.stringify(bodyPList) + '</div>\n';	// 增加上参数信息
        content = content.replace(p, table)
    })
    return content
}

// 加工md，将其中的@import  转换为真实内容
function refMd_import2content(content) {
    // 1、取出全文中所有的 ```d
    var reg = /@import\([\s\S]*?\)/gim // [\s\S]*=任意字符n次，?=非贪婪模式
    var importArr = content.match(reg) || [] // 返回所有匹配项数组

    // 遍历并转换
    importArr.forEach(function(import_item) {
        // 过滤空格
        // console.log(import_item);
        var item = import_item
            .replace(' ', '')
            .replace('@import(', '')
            .replace(')', '')

        // 开始替换
        content = content.replace(import_item, sa_mcontent[item] || '')
    })

    return content
}

// 加工md，将其中的```api 去除空格
function refMd_api2trim(content) {
    // 1、取出全文中所有的 ```d
    var reg = /```\s*api[\s\S]*?```/gim // [\s\S]*=任意字符n次，?=非贪婪模式
    var pArr = content.match(reg) || [] // 返回所有匹配项数组

    // 遍历并转换
    pArr.forEach(function(p) {
        // 将这个p内容按换行符切割
        var canStr = p
            .replace(/```\s*api/, '')
            .replace('```', '')
            .trim() // 去除首行尾行
        var canArr = canStr.split('\n') || [] // 按行切割
        var str = ''

        // 遍历并转换
        canArr.forEach(function(p) {
            str += p
        })

        str = '``` api\n' + str + '```\n'
            // console.log(str);

        // 加上按钮
        // str = '<button>测试接口</button>\n' + str;

        // 将原始p内容替换成为后来内容
        content = content.replace(p, str)
    })

    return content
}

// 加工md，将其中的变量替换
function refMd_var(content) {
    content = content.replace(
        '${sa_doc_cfg.server_url}',
        window.sa_doc_cfg.server_url
    )
    return content
}