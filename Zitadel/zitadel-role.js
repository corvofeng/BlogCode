let logger = require("zitadel/log")

function injectTencentRole(ctx, api) {
    // 1. 从 Org Metadata 中动态读取腾讯云 UIN
    let targetUIN = "";
    try {
        const orgMeta = ctx.v1.org.getMetadata();
        if (orgMeta && orgMeta.metadata) {
            // 查找 key 为 "tencent-uin" 的元数据
            const uinData = orgMeta.metadata.find(m => m.key === "tencent-uin");
            if (uinData) {
                // Zitadel 解析出来可能是数字，这里统一转成字符串
                targetUIN = String(uinData.value); 
            }
        }
    } catch (e) {
        // 如果读取失败，可以选择赋一个默认值或直接 return 拦截登录
        // targetUIN = "724581790"; 
    }

    // 如果组织没有配置 UIN，直接拦截，拒绝生成断言
    if (!targetUIN) {
        logger.log("No Tencent UIN found in Org Metadata. Skipping SAML Role injection.");
        return;
    }
    logger.log("Found Tencent UIN in Org Metadata: " + targetUIN);

    // 2. 权限映射表 (只映射角色后缀)
    const roleMapping = {
        "tc-admin-role": "admin-role",   
        "tc-dev-role":   "dev-role"      // 对应日志里发现的 tc-dev-role
    };

    // 3. 提取用户角色
    let userRoles = [];
    if (ctx.v1.user.grants && ctx.v1.user.grants.grants) {
        ctx.v1.user.grants.grants.forEach(grant => {
            if (grant.roles) {
                userRoles.push(...grant.roles);
            }
        });
    }
    logger.log("User Roles: " + JSON.stringify(userRoles));

    // 4. 匹配角色
    let targetRoleName = "";
    for (let i = 0; i < userRoles.length; i++) {
        let role = userRoles[i];
        if (roleMapping[role]) {
            targetRoleName = roleMapping[role];
            break;
        }
    }

    // 没匹配到腾讯云角色，拒绝生成断言
    if (!targetRoleName) return; 

    // 5. 动态拼装并注入 SAML
    const providerName = "Zitadel-IdP"; // 这个如果你也写在 Meta 里，同样可以动态读取
    const finalURN = `qcs::cam::uin/${targetUIN}:roleName/${targetRoleName},qcs::cam::uin/${targetUIN}:saml-provider/${providerName}`;

    api.v1.attributes.setCustomAttribute('https://cloud.tencent.com/SAML/Attributes/Role', "", finalURN);
    api.v1.attributes.setCustomAttribute('https://cloud.tencent.com/SAML/Attributes/RoleSessionName', "", ctx.v1.user.preferredUsername);
}