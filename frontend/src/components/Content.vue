<template>
    <div class="content">
       <slot></slot>
    </div>
</template>

<script>
    export default {
        name: "Content"
    }
</script>

<style scoped>
    .content {
        float: left;
        width: 90%;
        margin-top: 3%;
        margin-left: 10%;
        padding-left: 1%;
    }
</style>