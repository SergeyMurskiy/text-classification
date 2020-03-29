<template>
    <div class="menu">
        <b-nav vertical class="w-25">
            <b-nav-item active>
                <div class="menu-item">
                    <p class="h1 mb-2">
                        <b-icon icon="list-check" class="rounded-circle bg-danger p-2"></b-icon>
                    </p>
                    Классификация
                </div>
            </b-nav-item>
            <b-nav-item>
                <div class="menu-item">
                    <b-icon icon="gear-wide-connected" font-scale="3"
                            class="rounded-circle bg-danger p-2"></b-icon>
                    Обучение
                </div>
            </b-nav-item>
            <b-nav-item>
                <div class="menu-item">
                    <b-icon icon="graph-up" font-scale="3" class="rounded-circle bg-danger p-2"></b-icon>
                    Статистика
                </div>
            </b-nav-item>
            <b-nav-item>
                <div class="menu-item">
                    <b-icon icon="gear-wide-connected" font-scale="3"
                            class="rounded-circle bg-danger p-2"></b-icon>
                    Настройка моделей
                </div>
            </b-nav-item>
        </b-nav>
    </div>
</template>

<script>
    export default {
        name: 'Menu'
    }
</script>

<style scoped>
    .menu {
        float: left;
        width: 10%;
        height: 100vh;
        color: white;
        margin-top: 3%;
        position: fixed;
    }

    .menu-item {
        text-align: center;
        color: black;
    }

    .p-2 {
        color: white;
    }

    .p-2:hover {
        color: black;
    }
</style>
