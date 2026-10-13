# Axis — Distributed Server Monitoring Platform

[中文说明](#中文说明) · [Architecture](#architecture) · [Quick Start](#quick-start) · [Client Registration](#client-registration)

Axis is a lightweight, self-hosted platform for monitoring and managing multiple servers from a single web console. A Java client runs on every monitored host, collects system metrics, and reports them to a central Spring Boot service. The Vue dashboard provides real-time status, historical trends, host management, account permissions, and browser-based SSH access.

> This repository is a learning/personal project. Review the security configuration and replace all environment credentials before deploying it to a public or production environment.

## Features

- **Host monitoring:** CPU, memory, disk usage, disk I/O, network upload/download, OS, CPU, and hardware details.
- **Real-time and historical views:** shows current host state and renders metric history with ECharts.
- **Host management:** register, rename, group by location, and remove monitored hosts.
- **Access control:** JWT-based authentication with administrator and sub-account roles; sub-accounts can be restricted to assigned hosts.
- **Web terminal:** browser terminal built with WebSocket and xterm.js; the server opens the SSH connection to the target host.
- **Account services:** password reset and email verification support through Redis, RabbitMQ, and SMTP.

## Architecture

```text
                  HTTP metric reports
+-----------------------+        +-----------------------+
| monitor-client        |        | monitor-server        |
| Java 17 + OSHI        +------->| Spring Boot           |
| deployed per host     |        | MySQL / Redis         |
+-----------------------+        | InfluxDB / RabbitMQ   |
                                 +-----------+-----------+
                                             |
                                  REST API / WebSocket
                                             |
                                 +-----------v-----------+
                                 | monitor-web            |
                                 | Vue 3 + ECharts        |
                                 +-----------------------+
```

## Repository Layout

| Directory | Description |
| --- | --- |
| `monitor-client` | Java monitoring agent installed on each monitored host. |
| `monitor-server` | Central API, authentication, metric storage, and SSH terminal service. |
| `monitor-web` | Vue 3 management dashboard. |

## Technology Stack

- **Backend:** Java 17, Spring Boot, Spring Security, JWT, MyBatis-Plus, WebSocket, JSch
- **Storage & infrastructure:** MySQL, Redis, InfluxDB, RabbitMQ, SMTP
- **Client monitoring:** OSHI, Quartz
- **Frontend:** Vue 3, Vite, Element Plus, Pinia, Axios, ECharts, xterm.js

## Prerequisites

- JDK 17
- Maven 3.9+ (or an IDE with Maven support)
- Node.js 18+ and npm
- MySQL, Redis, InfluxDB, RabbitMQ, and an SMTP account if email verification is enabled

Before starting the server, update the selected environment configuration:

- `monitor-server/src/main/resources/application-dev.yml`
- `monitor-server/src/main/resources/application-prod.yml`

Configure your database, Redis, InfluxDB, RabbitMQ, SMTP, and a strong JWT signing key. Never commit production credentials.

> **Database schema:** this repository currently does not include database migration or SQL initialization files. Create/import the tables required by the DTOs and MyBatis mappers before starting the server.

## Quick Start

### 1. Start infrastructure services

Start MySQL, Redis, InfluxDB, and RabbitMQ, then update the active server profile with their connection details.

### 2. Start the server

```powershell
cd monitor-server
mvn spring-boot:run
```

For a production build:

```powershell
mvn clean package -Pprod
java -jar target/monitor-server-*.jar
```

The production profile is configured to listen on port `80`; choose a non-privileged port when appropriate for your environment.

### 3. Start the web dashboard

```powershell
cd monitor-web
npm install
npm run dev
```

Open the Vite URL printed by the command (normally `http://localhost:5173`).

When the frontend and backend are deployed on different origins, configure an Axios base URL or Vite proxy for `/api`, `/monitor`, and `/terminal`.

### 4. Prepare an administrator account

Create an administrator account in the database according to the account entity and Spring Security password format. The current repository contains login and password-reset endpoints but no public initial-registration screen.

## Client Registration

1. Log in as an administrator and select **Add Host** in the dashboard.
2. Copy the displayed server address and one-time registration token.
3. Build and run the client on the target machine:

   ```powershell
   cd monitor-client
   mvn clean package
   java -jar target/monitor-client-*.jar
   ```

4. On first launch, enter the server URL (for example, `http://monitor.example.com:8080`) and the registration token.
5. The client stores its connection configuration in `config/server.json` in its working directory and sends host details to the server.

After registration, use the dashboard to rename the host, set its location, view its status and trends, or configure an SSH connection.

## Security Notes

- Replace all sample/default configuration values, especially database credentials, SMTP credentials, and the JWT key.
- Serve the dashboard and APIs over HTTPS in real deployments.
- Restrict access to the WebSocket terminal endpoint and protect stored SSH credentials.
- The current terminal code disables SSH host-key verification. Enable host-key verification before any production use.

## Current Limitations

- The repository contains a `MonitorJobBean` but no Quartz `JobDetail`/`Trigger` registration. Add a scheduler before expecting automatic periodic runtime reporting.
- Network-interface selection is currently optimized for interfaces named `eth*` or `en*`; it may not work on every Windows, VPN, Docker, or custom-network environment.
- Database migrations and initial account creation are not included.

---

# 中文说明

Axis 是一个轻量级、自托管的多服务器监控与管理平台。每台被监控主机运行 Java 客户端，采集系统指标并上报到中心服务；Vue 管理后台用于查看实时状态、历史趋势、管理主机与账号，并提供网页 SSH 终端。

## 主要能力

- 采集 CPU、内存、磁盘、磁盘 I/O、网络上下行、操作系统及硬件信息。
- 展示实时状态和 ECharts 历史趋势。
- 注册、重命名、按地区分类和删除主机。
- 基于 JWT 的管理员/子账号权限体系，可将主机访问权限分配给子账号。
- 通过 WebSocket、xterm.js 和 JSch 提供网页 SSH 终端。
- 通过 Redis、RabbitMQ 与 SMTP 支持邮件验证码和密码重置。

## 快速开始

### 1. 准备基础服务

准备 MySQL、Redis、InfluxDB、RabbitMQ；如需邮箱验证码，还需配置 SMTP。然后按环境修改：

- `monitor-server/src/main/resources/application-dev.yml`
- `monitor-server/src/main/resources/application-prod.yml`

请替换所有数据库、消息队列、邮箱及 JWT 配置，勿将生产密钥提交到仓库。

> 当前仓库未提供建表 SQL 或数据库迁移文件。启动前需要根据 DTO 和 MyBatis Mapper 创建或导入所需表结构。

### 2. 启动后端与前端

```powershell
cd monitor-server
mvn spring-boot:run

cd ..\monitor-web
npm install
npm run dev
```

前后端不在同一域名时，需要为 `/api`、`/monitor` 和 `/terminal` 配置 Axios 基地址或 Vite 代理。

### 3. 注册被监控主机

1. 以管理员身份登录网页后台，点击 **添加新主机**。
2. 复制页面显示的服务端地址与一次性注册码。
3. 在目标主机安装 Java 17，构建并启动客户端：

   ```powershell
   cd monitor-client
   mvn clean package
   java -jar target/monitor-client-*.jar
   ```

4. 第一次运行时输入服务端 URL 和注册码。客户端会将连接信息保存在运行目录的 `config/server.json`。
5. 注册完成后即可在网页端查看主机、编辑节点信息、配置 SSH，以及查看实时和历史指标。

## 已知限制与安全提示

- `MonitorJobBean` 已实现指标采集，但仓库没有 Quartz 触发器注册代码；需补充定时调度后才能自动周期上报。
- 当前网卡筛选主要匹配 `eth*` 和 `en*`，在 Windows、VPN、Docker 等网络环境中可能需要调整。
- SSH 终端当前关闭了主机密钥校验；生产使用前应启用主机密钥验证，并妥善保护 SSH 凭据。

