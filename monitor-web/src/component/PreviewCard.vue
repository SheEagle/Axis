<script setup>

</script>

<template>
  <div class="instance-card">
    <div style="display: flex;justify-content: space-between">
      <div>
        <div class="name">
          <span class="flag-icon flag-icon-fr"></span>
          <span style="margin: 0 10px">云服务器</span>
          <i class="fa-solid fa-pen-to-square"></i>
        </div>

        <div class="os">
          操作系统：Ubuntu 20.04
        </div>
      </div>

      <div class="status">
        <i style="color: #18cb18" class="fa-solid fa-circle-play"></i>
        <span style="margin-left: 5px">运行中</span>
      </div>
    </div>
    <el-divider style="margin: 10px 0"></el-divider>
    <div class="network">
      <span style="margin-right: 10px">公网IP:192.168.0.11</span>
      <i class="fa-solid fa-copy" style="color: dodgerblue"></i>
    </div>
    <div class="cpu">
      <span style="margin-right: 10px">处理器: AMD</span>
    </div>

    <div class="hardware">
      <i class="fa-solid fa-microchip"></i>
      <span style="margin-right: 10px">2 CPU</span>
      <i class="fa-solid fa-memory"></i>
      <span>4 GB</span>
    </div>

    <div class="progress">
      <span>CPU: 2.5%</span>
      <el-progress :percentage="2.5" :stroke-width="5" :show-text="false"/>
    </div>

    <div class="progress">
      <span>内存: <b>1</b> GB</span>
      <el-progress :percentage="1/4*100" :stroke-width="5" :show-text="false"/>
    </div>

    <div class="network-flow">
      <div>网络流量</div>
      <div>
        <i class="fa-solid fa-arrow-up"></i>
        <span>12KB/s</span>
        <el-divider direction="vertical"/>
        <i class="fa-solid fa-arrow-down"></i>
        <span>12KB/s</span>
      </div>
    </div>

  </div>
</template>


<style lang="scss" scoped>
.dark .instance-card {
  color: #d9d9d9
}

.interact-item {
  transition: .3s;

  &:hover {
    cursor: pointer;
    scale: 1.1;
    opacity: 0.8;
  }
}

.instance-card {
  width: 320px;
  padding: 15px;
  background-color: var(--el-bg-color);
  border-radius: 5px;
  box-sizing: border-box;
  color: #606060;
  transition: .3s;

  &:hover {
    cursor: pointer;
    scale: 1.02;
  }

  .name {
    font-size: 15px;
    font-weight: bold;
  }

  .status {
    font-size: 14px;
  }

  .os {
    font-size: 13px;
    color: grey;
  }

  .network {
    font-size: 13px;
  }

  .hardware {
    margin-top: 5px;
    font-size: 13px;
  }

  .progress {
    margin-top: 10px;
    font-size: 12px;
  }

  .cpu {
    font-size: 13px;
  }

  .network-flow {
    margin-top: 10px;
    font-size: 12px;
    display: flex;
    justify-content: space-between;
  }
}
</style>