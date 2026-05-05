let logger = require("zitadel/log")

function debugSamlContext(ctx, api) {
    // 1. 打印基础 User 对象
    logger.log("=== Debug: User Info ===");
    logger.log(JSON.stringify(ctx.v1.user, null, 2));

    // 2. 打印用户的 Grants (角色和权限分配)
    logger.log("=== Debug: User Grants ===");
    if (ctx.v1.user.grants) {
        logger.log(JSON.stringify(ctx.v1.user.grants, null, 2));
    } else {
        logger.log("No grants found for this user.");
    }

    // 3. 打印 User 的 Metadata (注意 getMetadata 是一个函数)
    logger.log("=== Debug: User Metadata ===");
    try {
        let userMeta = ctx.v1.user.getMetadata();
        logger.log(JSON.stringify(userMeta, null, 2));
    } catch (e) {
        logger.log("Error reading User Metadata: " + e.message);
    }

    // 4. 打印基础 Org 对象
    logger.log("=== Debug: Org Info ===");
    logger.log(JSON.stringify(ctx.v1.org, null, 2));

    // 5. 打印 Org 的 Metadata
    logger.log("=== Debug: Org Metadata ===");
    try {
        let orgMeta = ctx.v1.org.getMetadata();
        logger.log(JSON.stringify(orgMeta, null, 2));
    } catch (e) {
        logger.log("Error reading Org Metadata: " + e.message);
    }

    // 注意：api 对象里面主要是函数（setCustomAttribute等），
    // 打印 api 本身通常看不到什么有用的数据，重点打印 ctx 即可。
}