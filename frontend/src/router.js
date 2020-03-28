import Vue from 'vue'
import Router from 'vue-router'
import LearningPage from './components/LearningPage'
import AnalyzePage from './components/AnalyzePage'
import WekaModelsPage from './components/WekaModelsPage'
import SelectModelPage from './components/SelectModelPage'
import StatisticPage from './components/StatisticPage'

Vue.use(Router);

export default new Router({
    mode: 'history',

    routes: [
        {
            path: '/learning',
            name: 'home',
            component: LearningPage
        },
        {
            path: '/learning',
            name: 'learning',
            component: AnalyzePage
        },
        {
            path: '/models',
            name: 'wekaModels',
            component: WekaModelsPage
        },
        {
            path: '/select/model',
            name: 'selectModel',
            component: SelectModelPage
        },
        {
            path: '/statistic',
            name: 'statistic',
            component: StatisticPage
        }
    ]
})