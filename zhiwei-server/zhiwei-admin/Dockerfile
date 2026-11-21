# 基础镜像
FROM eclipse-temurin:17-jdk-jammy
# 设定时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 敏感配置不写入镜像，构建/运行时通过 -e 或 --env-file 注入：
#   docker run -d --env-file .env -p 9000:9000 zhiwei-admin:tag
# 需要注入的变量见项目根目录 .env.example


# 拷贝jar包
COPY zhiwei-admin.jar /app.jar

# 暴露端口
EXPOSE 9000

# 入口
ENTRYPOINT ["java", "-jar", "/app.jar"]