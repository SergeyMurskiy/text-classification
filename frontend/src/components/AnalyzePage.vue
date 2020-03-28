<template>
    <div>
        <div class="card text-center">
            <div class="card-header">
                <ul class="nav nav-tabs card-header-tabs">
                    <li class="nav-item">
                        <a class="nav-link active" href="/analyze">Классификация</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/learning">Обучение</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/models">Weka Models</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="/select/model">Выбор модели</a>
                    </li>
                </ul>
            </div>
            <div class="card-body">
                <form>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio1"
                               value="elasticsearch" v-model="method" checked>
                        <label class="form-check-label" for="inlineRadio1">Elasticsearch</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio2"
                               value="j48" v-model="method">
                        <label class="form-check-label" for="inlineRadio2">Алгоритм С4.5</label>
                    </div>
                    <div class="form-check form-check-inline">
                        <input class="form-check-input" type="radio" name="inlineRadioOptions" id="inlineRadio3"
                               value="bayes" v-model="method">
                        <label class="form-check-label" for="inlineRadio3">Naive Bayes</label>
                    </div>
                    <hr>
                    <div class="form-row">
                        <div class="form-group charset">
                            <label for="inputState">Кодировка</label>
                            <select id="inputState" class="form-control" v-model="charset">
                                <option value="default" selected>UTF-8</option>
                                <option value="cp1251">cp1251</option>
                            </select>
                        </div>
                    </div>
                    <div class="choose">
                        <label for="files_input" class="btn btn-outline-primary btn-block"
                               v-bind:class="{ 'is-invalid': !isChooseFiles }">{{ fileCount }}</label>
                        <input type="file"
                               class="files_input"
                               id="files_input"
                               name="files_input"
                               ref="files_input" multiple
                               @change="checkFiles()">
                        <div class="invalid-feedback">
                            Необходимо выбрать файлы
                        </div>
                    </div>
                    <div class="clear-button">
                        <button type="button" class="btn btn-warning btn-block" @click="clearFields">Очистить</button>
                    </div>
                    <div class="submit-button">
                        <button type="button" class="btn btn-primary btn-block" :class="{'disabled': loading }"
                                @click="analyze">Отправить
                        </button>
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
                    <div>{{comment}}</div>
                    <div v-for="res in result" v-bind:key="res.fileName">
                        <ul class="list-group">
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                <b>{{res.fileName}}</b>
                                <span class="badge badge-primary badge-pill"></span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                Количество слов
                                <span class="badge badge-primary badge-pill">{{res.wordsCount}}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                Класс
                                <span class="badge badge-primary badge-pill">{{res.classValue}}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                Время
                                <span class="badge badge-primary badge-pill">{{res.milliseconds}}</span>
                            </li>
                            <li class="list-group-item d-flex justify-content-between align-items-center">
                                {{res.comment}}
                            </li>
                            <!--<li class="list-group-item d-flex justify-content-between align-items-center">-->
                            <!--Количество загруженных слов-->
                            <!--<span class="badge badge-primary badge-pill">{{result.countOfWords}}</span>-->
                            <!--</li>-->
                            <!--<li class="list-group-item d-flex justify-content-between align-items-center">-->
                            <!--Количество сохраненных токенов-->
                            <!--<span class="badge badge-primary badge-pill">{{result.wordsToSave}}</span>-->
                            <!--</li>-->
                            <!--<li class="list-group-item d-flex justify-content-between align-items-center">-->
                            <!--Время-->
                            <!--<span class="badge badge-primary badge-pill">{{result.time}} мс.</span>-->
                            <!--</li>-->
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: 'HelloWorld',
        props: {},
        data() {
            return {
                files: [],
                fileCount: 'Выберите файлы',
                charset: 'default',
                isChooseFiles: true,
                method: 'elasticsearch',
                loading: false,
                haveResponse: false,
                result: [],
                comment: ''
            }
        },

        methods: {
            checkFiles() {
                this.files = this.$refs.files_input.files;
                if (this.files.length === 0) {
                    this.isChooseFiles = false;
                    this.fileCount = 'Выберите файлы';
                    return false;
                } else {
                    this.isChooseFiles = true;
                    this.fileCount = 'Выбрано файлов: ' + this.files.length;
                    return true;
                }
            },

            clearFields() {
                this.files = null;
                this.isChooseFiles = true;
                this.fileCount = 'Выберите файлы'
            },

            learning() {
                if (!this.loading && (this.checkFiles())) {
                    this.haveResponse = false;
                    this.loading = true;

                    let analyzeUrl;
                    if (this.method === 'elasticsearch') {
                        analyzeUrl = this.url + "/learning/elasticsearch"
                    } else if (this.method === 'j48') {
                        analyzeUrl = this.url + "/learning/weka/j48"
                    } else if (this.method === 'bayes') {
                        analyzeUrl = this.url + "/learning/weka/bayes"
                    }

                    let formData = new FormData();

                    for (let i = 0; i < this.files.length; i++) {
                        formData.append('files', this.files[i]);
                    }

                    formData.append('charset', this.charset);

                    axios.post(analyzeUrl, formData, {
                        headers: {
                            'Content-Type': 'multipart/form-data'
                        }
                    }).then((response) => {
                        this.loading = false;
                        this.haveResponse = true;
                        this.comment = response.data.comment;
                        this.result = response.data.analyzeResponseEntities;
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

    .files_input {
        width: 0.1px;
        height: 0.1px;
        opacity: 0;
        overflow: hidden;
        position: absolute;
        z-index: -1;
    }

    .submit-button {
        width: 23.5%;
        float: left;
    }

    .is-invalid {
        display: block;
        color: #D3243B;
        border-color: #D3243B;
    }

    .btn-outline-primary:hover {
        color: white;
    }

    .choose {
        float: left;
        width: 49%;
        margin-right: 2%;
    }

    .topic {
        width: 49%;
        margin-right: 2%;
    }

    .charset {
        width: 49%;
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
        margin-top: 10%;
    }

    .list-group {
        margin-bottom: 1%;
    }
</style>
