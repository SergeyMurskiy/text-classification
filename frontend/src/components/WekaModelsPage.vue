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
                        <a class="nav-link active" href="/models">Weka Models</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/select/model">Выбор модели</a>
                    </li>
                </ul>
            </div>
            <div class="card-body">
                <form>
                    <div class="form-row">
                        <div class="form-group topic">
                            <label for="modelName">Название модели</label>
                            <input type="text" class="form-control"
                                   id="modelName"
                                   v-model="modelName"
                                   v-bind:class="{ 'is-invalid': !isCorrectModelName }">
                            <div class="invalid-feedback">
                                Укажите название модели
                            </div>
                        </div>
                        <div class="form-group charset">
                            <label for="sizeOfAttributes">Количество атрибутов</label>
                            <input type="text" class="form-control"
                                   id="sizeOfAttributes"
                                   v-model="sizeOfAttributes"
                                   v-bind:class="{ 'is-invalid': !isCorrectModelName }">
                            <div class="invalid-feedback">
                                Укажите тему
                            </div>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-check">
                            <input class="form-check-input"
                                   type="checkbox"
                                   v-model="setActive"
                                   id="defaultCheck1">
                            <label class="form-check-label" for="defaultCheck1">
                                Сделать активной моделью
                            </label>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="submit-button">
                            <button type="button" class="btn btn-primary btn-block" :class="{'disabled': loading }"
                                    @click="analyze">Сохранить
                            </button>
                        </div>
                    </div>
                </form>
                <div class="loading" v-show="loading">
                    <hr>
                    <div class="spinner-border m-5" role="status">
                        <span class="sr-only">Loading...</span>
                    </div>
                </div>
                <div v-show="haveResponse" class="response">
                    <hr>
                    <ul class="list-group">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <p>{{response}}</p>
                            <span class="badge badge-primary badge-pill"></span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: 'WekaModelsPage',
        props: {},
        data() {
            return {
                loading: false,
                modelName: '',
                sizeOfAttributes: 100,
                setActive: true,
                haveResponse: false,
                response: '',
                isCorrectModelName: true,
            }
        },

        methods: {
            learning() {
                if (!this.loading) {
                    this.haveResponse = false;
                    this.loading = true;

                    let analyzeUrl = this.url + "/weka/model/build/random";

                    let formData = new FormData();
                    formData.append('modelName', this.modelName);
                    formData.append('sizeOfAttributes', this.sizeOfAttributes);
                    formData.append('setActive', this.setActive);

                    axios.post(analyzeUrl, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    }).then((response) => {
                        this.loading = false;
                        this.haveResponse = true;
                        this.response = response.data;
                        console.log(response)
                    }).catch(() => {
                        this.haveResponse = true;
                    })
                }
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
        margin-top: 2%;
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


    .topic {
        float: left;
        width: 49%;
        margin-right: 2%;
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
    .set-active{
        text-align: left;
    }
</style>
