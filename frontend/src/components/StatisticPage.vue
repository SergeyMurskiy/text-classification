<template>
    <div>
        <Menu></Menu>
        <Content>
            <div class="from mb-sm-2">
                <div class="mr-sm-2 first-item">
                    <label>Методы</label>
                    <b-form-select v-model="methodNames" :options="methods" multiple :select-size="3"></b-form-select>
                </div>
                <div class="mr-sm-2 first-item">
                    <label>Темы</label>
                    <b-form-select v-model="selectTopics" :options="topics" multiple :select-size="3"></b-form-select>
                </div>
                <div class="second-item">
                    <label>Статус</label>
                    <b-form-radio v-model="status" name="some-radios" value="correct" select>Успешно</b-form-radio>
                    <b-form-radio v-model="status" name="some-radios" value="incorrect">Неуспешно</b-form-radio>
                </div>
                <div class="first-item mr-sm-2">
                    <label>Тип графика</label>
                    <b-form-select v-model="graphType" :options="graphTypes"></b-form-select>
                </div>
                <div class="first-item button">
                    <b-button class="button" variant="primary" @click="kibana">Показать</b-button>
                </div>
            </div>
            <iframe v-if="show" :key="renderKey" :src="url" class="dashboard"></iframe>
            <iframe :src="urlTime" class="dashboard"></iframe>
        </Content>
    </div>
</template>

<script>
    import axios from 'axios'

    export default {
        name: 'StatisticPage',
        components: [],
        props: {},
        data() {
            return {
                files: [],
                selectTopics: ['все'],
                topics: ['все', 'история', 'математика', 'медицина'],
                methodNames: ['все'],
                methods: ['все'],
                url: "http://localhost:5601/app/kibana#/visualize/edit/AXD4U-nztxCaZLmAEH1z?embed=true&_g=(filters:!(),refreshInterval:('$$hashKey':'object:32955',display:'5+seconds',pause:!f,section:1,value:5000),time:(from:now%2Fw,mode:quick,to:now%2Fw))&_a=(filters:!(),linked:!f,query:(query_string:(query:'status:correct')),uiState:(vis:(colors:(elasticsearch:%23CCA300,%D0%BC%D0%B0%D1%82%D0%B5%D0%BC%D0%B0%D1%82%D0%B8%D0%BA%D0%B0:%23511749,%D0%BC%D0%B5%D0%B4%D0%B8%D1%86%D0%B8%D0%BD%D0%B0:%23F2C96D))),vis:(aggs:!((enabled:!t,id:'1',params:(customLabel:correct),schema:metric,type:count),(enabled:!t,id:'2',params:(extended_bounds:(max:1,min:1),field:countOfTexts,interval:1,min_doc_count:!t),schema:segment,type:histogram),(enabled:!t,id:'3',params:(field:methodName.keyword,order:desc,orderBy:'1',size:10000),schema:group,type:terms)),listeners:(),params:(addLegend:!t,addTimeMarker:!f,addTooltip:!t,categoryAxes:!((id:CategoryAxis-1,labels:(filter:!f,rotate:0,show:!f,truncate:100),position:bottom,scale:(type:linear),show:!t,style:(),title:(text:countOfTexts),type:category)),grid:(categoryLines:!f,style:(color:%23eee),valueAxis:ValueAxis-1),legendPosition:right,orderBucketsBySum:!f,seriesParams:!((data:(id:'1',label:correct),drawLinesBetweenPoints:!t,interpolate:linear,lineWidth:2,mode:normal,show:true,showCircles:!t,type:line,valueAxis:ValueAxis-1)),times:!(),type:line,valueAxes:!((id:ValueAxis-1,labels:(filter:!f,rotate:0,show:!t,truncate:100),name:LeftAxis-1,position:left,scale:(max:100,min:0,mode:normal,setYExtents:!t,type:linear),show:!t,style:(),title:(text:correct),type:value))),title:correct-statistic-math,type:line))",
                // urlTime: "http://localhost:5601/app/kibana#/visualize/edit/AXD5GgWPtxCaZLmAEH2m?embed=true&_g=()&_a=(filters:!(),linked:!f,query:(match_all:()),uiState:(vis:(colors:(elasticsearch:%23CCA300,naive-bayes:%236D1F62))),vis:(aggs:!((enabled:!t,id:'1',params:(customLabel:'%D0%A1%D1%80%D0%B5%D0%B4%D0%BD%D0%B5%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B+%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%B0',field:time),schema:metric,type:avg),(enabled:!t,id:'2',params:(customLabel:'%D0%9A%D0%BE%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D1%82%D0%B2%D0%BE+%D1%82%D0%B5%D0%BA%D1%81%D1%82%D0%BE%D0%B2+%D0%B2+%D0%BE%D0%B1%D1%83%D1%87%D0%B0%D1%8E%D1%89%D0%B5%D0%B9+%D0%B2%D1%8B%D0%B1%D0%BE%D1%80%D0%BA%D0%B5',extended_bounds:(max:1,min:1),field:countOfTexts,interval:1,min_doc_count:!t),schema:segment,type:histogram),(enabled:!t,id:'3',params:(field:methodName.keyword,order:desc,orderBy:'1',size:5),schema:group,type:terms)),listeners:(),params:(addLegend:!t,addTimeMarker:!f,addTooltip:!t,categoryAxes:!((id:CategoryAxis-1,labels:(show:!f,truncate:100),position:bottom,scale:(type:linear),show:!t,style:(),title:(text:'%D0%9A%D0%BE%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D1%82%D0%B2%D0%BE+%D1%82%D0%B5%D0%BA%D1%81%D1%82%D0%BE%D0%B2+%D0%B2+%D0%BE%D0%B1%D1%83%D1%87%D0%B0%D1%8E%D1%89%D0%B5%D0%B9+%D0%B2%D1%8B%D0%B1%D0%BE%D1%80%D0%BA%D0%B5'),type:category)),grid:(categoryLines:!f,style:(color:%23eee)),legendPosition:right,seriesParams:!((data:(id:'1',label:'%D0%A1%D1%80%D0%B5%D0%B4%D0%BD%D0%B5%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B+%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%B0'),drawLinesBetweenPoints:!t,mode:normal,show:true,showCircles:!t,type:line,valueAxis:ValueAxis-1)),times:!(),type:line,valueAxes:!((id:ValueAxis-1,labels:(filter:!f,rotate:0,show:!t,truncate:100),name:LeftAxis-1,position:left,scale:(mode:normal,type:linear),show:!t,style:(),title:(text:'%D0%A1%D1%80%D0%B5%D0%B4%D0%BD%D0%B5%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B+%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%B0'),type:value))),title:'%D0%92%D1%80%D0%B5%D0%BC%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B+%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%B0',type:line))",
                urlTime: "http://localhost:5601/app/kibana#/dashboard/AXElwt9sz8JZFj3YviJa?embed=true&_g=(refreshInterval:('$$hashKey':'object:5028',display:'5+seconds',pause:!f,section:1,value:5000),time:(from:now-15m,mode:quick,to:now))&_a=(description:'',filters:!(),options:(darkTheme:!f),panels:!((col:1,id:AXD5GgWPtxCaZLmAEH2m,panelIndex:1,row:1,size_x:12,size_y:4,type:visualization)),query:(topic:+история),timeRestore:!f,title:'New+Dashboard1',uiState:(),viewMode:view)",
                renderKey: 1,
                show: true,
                status: 'correct',
                graphType: 'точность работы',
                graphTypes: ["точность работы", "время работы"]
            }
        },

        mounted() {
            axios.get(this.serverUrl + "/statistic/info/methods")
                .then((response) => {
                    for (let i = 0; i < response.data.length; i++) {
                        this.methods.push(response.data[i]);
                    }
                });
        },

        methods: {
            kibana() {
                const firstPart = "http://localhost:5601/app/kibana#/visualize/edit/AXD4U-nztxCaZLmAEH1z?embed=true&_g=(filters:!(),refreshInterval:('$$hashKey':'object:32955',display:'5+seconds',pause:!f,section:1,value:5000),time:(from:now%2Fw,mode:quick,to:now%2Fw))&_a=(filters:!(),linked:!f,query:(query_string:(query:'";
                const secondPart = "')),uiState:(vis:(colors:(elasticsearch:%23CCA300,%D0%BC%D0%B0%D1%82%D0%B5%D0%BC%D0%B0%D1%82%D0%B8%D0%BA%D0%B0:%23511749,%D0%BC%D0%B5%D0%B4%D0%B8%D1%86%D0%B8%D0%BD%D0%B0:%23F2C96D))),vis:(aggs:!((enabled:!t,id:'1',params:(customLabel:correct),schema:metric,type:count),(enabled:!t,id:'2',params:(extended_bounds:(max:1,min:1),field:countOfTexts,interval:1,min_doc_count:!t),schema:segment,type:histogram),(enabled:!t,id:'3',params:(field:methodName.keyword,order:desc,orderBy:'1',size:10000),schema:group,type:terms)),listeners:(),params:(addLegend:!t,addTimeMarker:!f,addTooltip:!t,categoryAxes:!((id:CategoryAxis-1,labels:(filter:!f,rotate:0,show:!f,truncate:100),position:bottom,scale:(type:linear),show:!t,style:(),title:(text:'countOfTexts'),type:category)),grid:(categoryLines:!f,style:(color:%23eee),valueAxis:ValueAxis-1),legendPosition:right,orderBucketsBySum:!f,seriesParams:!((data:(id:'1',label:correct),drawLinesBetweenPoints:!t,interpolate:linear,lineWidth:2,mode:normal,show:true,showCircles:!t,type:line,valueAxis:ValueAxis-1)),times:!(),type:line,valueAxes:!((id:ValueAxis-1,labels:(filter:!f,rotate:0,show:!t,truncate:100),name:LeftAxis-1,position:left,scale:(max:";
                const lastPart = ",min:0,mode:normal,setYExtents:!t,type:linear),show:!t,style:(),title:(text:correct),type:value))),title:correct-statistic-math,type:line))";
                // const url = "http://localhost:5601/app/kibana#/dashboard/AXElwt9sz8JZFj3YviJa?embed=true&_g=(refreshInterval:('$$hashKey':'object:5028',display:'5+seconds',pause:!f,section:1,value:5000),time:(from:now-15m,mode:quick,to:now))&_a=(description:'',filters:!(),options:(darkTheme:!f),panels:!((col:1,id:AXD5GgWPtxCaZLmAEH2m,panelIndex:1,row:1,size_x:12,size_y:4,type:visualization)),query:(match_all:()),timeRestore:!f,title:'New+Dashboard1',uiState:(),viewMode:view)";
                // const timeFirstPath = "http://localhost:5601/app/kibana#/visualize/edit/AXD5GgWPtxCaZLmAEH2m?embed=true&_g=()&_a=(filters:!(),linked:!f,query:(match_all:()),uiState:(vis:(colors:(elasticsearch:%23CCA300,naive-bayes:%236D1F62))),vis:(aggs:!((enabled:!t,id:'1',params:(customLabel:'%D0%A1%D1%80%D0%B5%D0%B4%D0%BD%D0%B5%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B+%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%B0',field:time),schema:metric,type:avg),(enabled:!t,id:'2',params:(customLabel:'%D0%9A%D0%BE%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D1%82%D0%B2%D0%BE+%D1%82%D0%B5%D0%BA%D1%81%D1%82%D0%BE%D0%B2+%D0%B2+%D0%BE%D0%B1%D1%83%D1%87%D0%B0%D1%8E%D1%89%D0%B5%D0%B9+%D0%B2%D1%8B%D0%B1%D0%BE%D1%80%D0%BA%D0%B5',extended_bounds:(max:1,min:1),field:countOfTexts,interval:1,min_doc_count:!t),schema:segment,type:histogram),(enabled:!t,id:'3',params:(field:methodName.keyword,order:desc,orderBy:'1',size:5),schema:group,type:terms)),listeners:(),params:(addLegend:!t,addTimeMarker:!f,addTooltip:!t,categoryAxes:!((id:CategoryAxis-1,labels:(show:!f,truncate:100),position:bottom,scale:(type:linear),show:!t,style:(),title:(text:'%D0%9A%D0%BE%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D1%82%D0%B2%D0%BE+%D1%82%D0%B5%D0%BA%D1%81%D1%82%D0%BE%D0%B2+%D0%B2+%D0%BE%D0%B1%D1%83%D1%87%D0%B0%D1%8E%D1%89%D0%B5%D0%B9+%D0%B2%D1%8B%D0%B1%D0%BE%D1%80%D0%BA%D0%B5'),type:category)),grid:(categoryLines:!f,style:(color:%23eee)),legendPosition:right,seriesParams:!((data:(id:'1',label:'%D0%A1%D1%80%D0%B5%D0%B4%D0%BD%D0%B5%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B+%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%B0'),drawLinesBetweenPoints:!t,mode:normal,show:true,showCircles:!t,type:line,valueAxis:ValueAxis-1)),times:!(),type:line,valueAxes:!((id:ValueAxis-1,labels:(filter:!f,rotate:0,show:!t,truncate:100),name:LeftAxis-1,position:left,scale:(mode:normal,type:linear),show:!t,style:(),title:(text:'%D0%A1%D1%80%D0%B5%D0%B4%D0%BD%D0%B5%D0%B5+%D0%B2%D1%80%D0%B5%D0%BC%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B+%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%B0'),type:value))),title:'%D0%92%D1%80%D0%B5%D0%BC%D1%8F+%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D1%8B+%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%B0',type:line))";
                let query = "status:+" + this.status;
                if (!this.selectTopics.includes("все")) {
                    query += "+AND+(";

                    for (let i = 0; i < this.selectTopics.length; i++) {
                        if (i < this.selectTopics.length - 1) {
                            query += "topic:+" + this.selectTopics[i] + "+OR+";
                        } else {
                            query += "topic:+" + this.selectTopics[i];
                        }
                    }
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

                let size;
                if (this.selectTopics.includes("все")) {
                    size = 100;
                } else {
                    size = 33 * this.selectTopics.length + 1;
                }

                this.show = false;
                this.url = firstPart + query + secondPart + size + lastPart;
                this.renderKey++;
                this.show = true;
            }
        }
    }
</script>

<style scoped>
    .dashboard {
        width: 100%;
        height: 300px;
    }

    .type {
        width: 20%;
        /*clear: both;*/
        float: left;
    }

    .type-item {
        float: left;
    }

    .button {
        margin-top: 1em;
    }

    .from {
        float: left;
        width: 100%;
    }

    .first-item {
        float: left;
        width: 20%;
    }

    .second-item {
        float: left;
        width: 10%;
    }
</style>
