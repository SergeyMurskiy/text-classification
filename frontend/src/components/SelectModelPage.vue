<template>
    <div>
        <div class="card text-center">
            <div class="card-header">
                <ul class="nav nav-tabs card-header-tabs">
                    <li class="nav-item">
                        <a class="nav-link" href="/analyze">Классификация</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/learning">Обучение</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/models">Weka Models</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link active" href="/select/model">Выбор модели</a>
                    </li>
                </ul>
            </div>
            <div class="card-body">
                <form>
                    <div class="form-group">
                        <div class="form-row">
                            <div class="model-name">
                                <label for="modelName">Название модели</label>
                                <input type="text" class="form-control"
                                       id="modelName"
                                       v-model="modelName">
                            </div>
                            <div class="submit-button">
                                <button type="button" class="btn btn-primary btn-block" :class="{'disabled': loading }"
                                        @click="analyze">Поиск
                                </button>
                            </div>
                        </div>
                    </div>
                    <div v-for="model in models" v-bind:key="model.modelName">
                        <hr>
                        <ul class="list-group model">
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                <b>{{model.modelName}}</b>
                                <span>
                                    <div class="btn-group mr-2" role="group">
                                          <button type="button" class="btn btn-danger btn-sm model-buttons"
                                                  @click="deleteModel(model.modelName)">Удалить
                                          </button>
                                    </div>
                                    <div class="btn-group" role="group">
                                         <button type="button" class="btn btn-success btn-sm model-buttons"
                                                 :class="{'disabled': loading }"
                                                 @click="setActiveModel(model.modelName)">Сделать активной
                                          </button>
                                    </div>
                                </span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                Количество закруженных текстов
                                <span class="badge badge-primary badge-pill">{{model.sizeOfData}}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                Количество загруженных слов
                                <span class="badge badge-primary badge-pill">{{model.countOfWords}}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                Классы
                                <div>
                                    <span class="badge badge-primary badge-pill" v-for="class1 in model.classes"
                                          v-bind:key="class1">
                                        {{class1}}
                                    </span>
                                </div>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                Активна
                                <span class="badge badge-primary badge-pill">{{model.active}}</span>
                            </li>
                            <li v-if="model.dataStatistic != null" class="list-group-item d-flex  align-items-center">
                                <div class="data-statistic">
                                    <div v-for="(value, name) in model.dataStatistic" v-bind:key="name">
                                        <b>{{ name }}</b>: {{ value }}
                                    </div>
                                </div>
                            </li>
                        </ul>
                    </div>
                </form>
            </div>
        </div>
        <!--<div aria-live="polite" aria-atomic="true" style="position: relative; min-height: 200px;">-->
            <!--<div class="toast" style="position: absolute; top: 0; right: 0;">-->
                <!--<div class="toast-header">-->
                    <!--<strong class="mr-auto">Bootstrap</strong>-->
                    <!--<small>11 mins ago</small>-->
                    <!--<button type="button" class="ml-2 mb-1 close" data-dismiss="toast" aria-label="Close">-->
                        <!--<span aria-hidden="true">&times;</span>-->
                    <!--</button>-->
                <!--</div>-->
                <!--<div class="toast-body">-->
                    <!--Hello, world! This is a toast message.-->
                <!--</div>-->
            <!--</div>-->
        <!--</div>-->
    </div>
</template>

<script>
    import axios from 'axios'
    export default {
        name: 'SelectModelPage',
        props: {},
        data() {
            return {
                loading: false,
                sizeOfAttributes: 100,
                setActive: true,
                models: [],
                modelName: "",
            }
        },

        mounted() {
            this.getModels();
        },

        methods: {
            getModels() {
                axios.get(this.url + "/weka/model/all")
                    .then((response) => {
                        this.models = response.data;
                    }).catch(() => {
                });
            },
            learning() {
                if (!this.loading) {
                    this.loading = true;

                    let analyzeUrl = this.url + "/weka/model/change";

                    let formData = new FormData();
                    formData.append('modelName', this.modelName);
                    console.log(this.modelName);
                    axios.post(analyzeUrl, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    }).then(() => {
                        this.loading = false;
                    }).catch(() => {
                    })
                }
            },

            makeToast(append = false) {
                this.$bvToast.toast(`This is toast number`, {
                    title: 'BootstrapVue Toast',
                    autoHideDelay: 5000,
                    appendToast: append
                })
            },

            deleteModel(modelName) {
                let analyzeUrl = this.url + "/weka/model/delete";
                //
                // let formData = new FormData();
                // formData.append('modelName', modelName);

                axios.delete(analyzeUrl, {
                    params: {
                        modelName: modelName
                    },
                }).then(() => {
                    this.makeToast();
                    this.getModels();
                }).catch(() => {
                })
            },

            setActiveModel(modelName) {
                let formData = new FormData();
                formData.append('modelName', modelName);

                let analyzeUrl = this.url + "/weka/model/change";
                axios.post(analyzeUrl, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                }).then(() => {
                    this.makeToast();
                    this.getModels();
                }).catch(() => {
                })
            }

        }
    }
</script>

<style scoped>
    .card {
        width: 50%;
        margin: 2% auto;
    }

    .card-body {
        text-align: left;

    }

    .form-row {
        margin: 0px;
    }


    .submit-button {
        margin-top: 4.8%;
        width: 23.5%;
    }

    .is-invalid {
        display: block;
        color: #D3243B;
        border-color: #D3243B;
    }

    .btn-outline-primary:hover {
        color: white;
    }


    .charset {
        width: 25%;
    }

    .clear-button {
        width: 23.5%;
        float: left;
        margin-right: 2%;
    }

    .loading {
        text-align: center;
        margin-top: 10%;
    }

    .disabled {
        cursor: default;
    }

    .response {
        margin-top: 2%;
    }

    .list-group {
        margin-bottom: 1%;
    }

    .model {
        width: 100%;
    }

    .model-name {
        float: left;
        /*width: 49%;*/
        margin-right: 2%;
    }

    .model-buttons {

    }
    .buttons {
        //width: 30%;
    }
</style>
