<template>
    <div>
        <div>
            <b-form inline>
                <b-form-group label="Методы">
                    <b-form-select
                            v-model="methodNames"
                            :options="methods"
                            :select-size="3"
                            class="butons mr-sm-2"
                            multiple
                    ></b-form-select>
                </b-form-group>
                <b-form-group label="Темы">
                    <b-form-select
                            v-model="selectTopics"
                            :options="topics"
                            :select-size="3"
                            class="butons mr-sm-2"
                            multiple
                    ></b-form-select>
                </b-form-group>
                <b-form-group class="mb-sm-4 mr-sm-2" label="Статус">
                    <b-form-radio v-model="status" name="some-radios" value="correct" select>Успешно</b-form-radio>
                    <b-form-radio v-model="status" name="some-radios" value="incorrect">Неуспешно</b-form-radio>
                </b-form-group>
                <b-button variant="primary" class="mt-sm-4" @click="kibana">Показать</b-button>
            </b-form>
        </div>
        <iframe v-if="show" :key="renderKey" :src="url" class="dashboard"></iframe>
    </div>
</template>

<script>
    // import axios from 'axios'
    import BForm from 'bootstrap-vue'
    import BFormInput from 'bootstrap-vue'
    import BFormSelect from 'bootstrap-vue'

    export default {
        name: 'StatisticPage',
        components: [
            BForm,
            BFormInput,
            BFormSelect
        ],
        props: {},
        data() {
            return {
                files: [],
                selectTopics: ['все'],
                topics: ['все', 'история', 'математика', 'медицина'],
                methodNames: ['все'],
                methods: ['все', 'elasticsearch', 'naive-bayes'],
                url: "http://localhost:5601/app/kibana#/visualize/edit/AXD4U-nztxCaZLmAEH1z?embed=true&_g=(filters:!(),refreshInterval:('$$hashKey':'object:32955',display:'5+seconds',pause:!f,section:1,value:5000),time:(from:now%2Fw,mode:quick,to:now%2Fw))&_a=(filters:!(),linked:!f,query:(query_string:(query:'status:correct')),uiState:(vis:(colors:(elasticsearch:%23CCA300,%D0%BC%D0%B0%D1%82%D0%B5%D0%BC%D0%B0%D1%82%D0%B8%D0%BA%D0%B0:%23511749,%D0%BC%D0%B5%D0%B4%D0%B8%D1%86%D0%B8%D0%BD%D0%B0:%23F2C96D))),vis:(aggs:!((enabled:!t,id:'1',params:(customLabel:correct),schema:metric,type:count),(enabled:!t,id:'2',params:(extended_bounds:(max:1,min:1),field:countOfTexts,interval:1,min_doc_count:!t),schema:segment,type:histogram),(enabled:!t,id:'3',params:(field:methodName.keyword,order:desc,orderBy:'1',size:10000),schema:group,type:terms)),listeners:(),params:(addLegend:!t,addTimeMarker:!f,addTooltip:!t,categoryAxes:!((id:CategoryAxis-1,labels:(filter:!f,rotate:0,show:!f,truncate:100),position:bottom,scale:(type:linear),show:!t,style:(),title:(text:countOfTexts),type:category)),grid:(categoryLines:!f,style:(color:%23eee),valueAxis:ValueAxis-1),legendPosition:right,orderBucketsBySum:!f,seriesParams:!((data:(id:'1',label:correct),drawLinesBetweenPoints:!t,interpolate:linear,lineWidth:2,mode:normal,show:true,showCircles:!t,type:line,valueAxis:ValueAxis-1)),times:!(),type:line,valueAxes:!((id:ValueAxis-1,labels:(filter:!f,rotate:0,show:!t,truncate:100),name:LeftAxis-1,position:left,scale:(max:100,min:0,mode:normal,setYExtents:!t,type:linear),show:!t,style:(),title:(text:correct),type:value))),title:correct-statistic-math,type:line))",
                renderKey: 1,
                show: true,
                status: 'correct'
            }
        },

        methods: {
            kibana() {
                const firstPart = "http://localhost:5601/app/kibana#/visualize/edit/AXD4U-nztxCaZLmAEH1z?embed=true&_g=(filters:!(),refreshInterval:('$$hashKey':'object:32955',display:'5+seconds',pause:!f,section:1,value:5000),time:(from:now%2Fw,mode:quick,to:now%2Fw))&_a=(filters:!(),linked:!f,query:(query_string:(query:'";
                const lastPart = "')),uiState:(vis:(colors:(elasticsearch:%23CCA300,%D0%BC%D0%B0%D1%82%D0%B5%D0%BC%D0%B0%D1%82%D0%B8%D0%BA%D0%B0:%23511749,%D0%BC%D0%B5%D0%B4%D0%B8%D1%86%D0%B8%D0%BD%D0%B0:%23F2C96D))),vis:(aggs:!((enabled:!t,id:'1',params:(customLabel:correct),schema:metric,type:count),(enabled:!t,id:'2',params:(extended_bounds:(max:1,min:1),field:countOfTexts,interval:1,min_doc_count:!t),schema:segment,type:histogram),(enabled:!t,id:'3',params:(field:methodName.keyword,order:desc,orderBy:'1',size:10000),schema:group,type:terms)),listeners:(),params:(addLegend:!t,addTimeMarker:!f,addTooltip:!t,categoryAxes:!((id:CategoryAxis-1,labels:(filter:!f,rotate:0,show:!f,truncate:100),position:bottom,scale:(type:linear),show:!t,style:(),title:(text:countOfTexts),type:category)),grid:(categoryLines:!f,style:(color:%23eee),valueAxis:ValueAxis-1),legendPosition:right,orderBucketsBySum:!f,seriesParams:!((data:(id:'1',label:correct),drawLinesBetweenPoints:!t,interpolate:linear,lineWidth:2,mode:normal,show:true,showCircles:!t,type:line,valueAxis:ValueAxis-1)),times:!(),type:line,valueAxes:!((id:ValueAxis-1,labels:(filter:!f,rotate:0,show:!t,truncate:100),name:LeftAxis-1,position:left,scale:(max:100,min:0,mode:normal,setYExtents:!t,type:linear),show:!t,style:(),title:(text:correct),type:value))),title:correct-statistic-math,type:line))";

                let query = "status:+" + this.status;
                if (!this.selectTopics.includes("все")) {
                    query += "+AND+("

                    for (let i = 0; i < this.selectTopics.length; i++) {
                        if (i < this.selectTopics.length - 1) {
                            query += "topic:+" + this.selectTopics[i] + "+OR+";
                        } else {
                            query += "topic:+" + this.selectTopics[i];
                        }                    }
                    query += ")";
                }

                if (!this.methodNames.includes("все")) {
                    query += "+AND+(";

                    for (let i = 0; i < this.methodNames.length; i++) {
                        if (i < this.methodNames.length - 1) {
                            query += "methodName:+" + this.methodNames[i] + "+OR+";
                        } else {
                            query += "methodName:+" + this.methodNames[i];
                        }
                    }
                        query += ")";
                }

                this.show = false;
                this.url = firstPart + query + lastPart;
                this.renderKey++;
                this.show = true;
            }
        }
    }
</script>

<style scoped>
    .dashboard {
        position: absolute;
        width: 100%;
        height: 700px;
    }

    .butons {
        width: 20em;
    }
</style>
