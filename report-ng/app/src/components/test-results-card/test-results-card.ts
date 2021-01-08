/*
 * Testerra
 *
 * (C) 2020, Mike Reiche, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import {autoinject, bindable} from "aurelia-framework";
import {StatusConverter} from "../../services/status-converter";
import {StatisticsGenerator} from "../../services/statistics-generator";
import {ExecutionStatistics} from "../../services/statistic-models";
import {ApexOptions} from "apexcharts";

@autoinject
export class TestResultsCard {
    @bindable result;
    @bindable executionStatistics: ExecutionStatistics;
    private _apexPieOptions: ApexOptions = undefined;
    private _selection;
    private _overallExitPoints;
    private _overallFailureAspects;


    constructor(
        private _statusConverter: StatusConverter,
        private _statisticsGenerator: StatisticsGenerator,
        private _element: Element
    ) {
    }

    resultChanged(){
        if (this.result.status) {
            // console.log("result status selected: " + this._statusConverter.getLabelForStatus(this.result.status));
            //get datapoint index to select from filter
            let label: string = this._statusConverter.getLabelForStatus(this.result.status);
            //pass index to apexchart-element
            this._selection = { dataPointIndex: this._apexPieOptions.labels.indexOf(label) };
        }

    }

    executionStatisticsChanged() {
        this._preparePieChart(this.executionStatistics);
        //this._overallExitPoints = this.executionStatistics.exitPointStatistics.length;
        this._overallFailureAspects = this.executionStatistics.failureAspectStatistics.length;
    }

    private _preparePieChart(executionStatistics: ExecutionStatistics): void {
        const series = [];
        const labels = [];
        const labelStatus = [];
        const colors = [];

        for (const status of this._statusConverter.relevantStatuses) {
            series.push(executionStatistics.getStatusCount(status));
            labels.push(this._statusConverter.getLabelForStatus(status));
            labelStatus.push(status)
            colors.push(this._statusConverter.getColorForStatus(status));
        }

        this._apexPieOptions = {
            chart: {
                type: 'pie',
                //width: '400px',
                height:'300px',
                fontFamily: 'Roboto',
                events: {
                    dataPointSelection: (event, chartContext, config) => {
                        this._piePieceClicked(labelStatus[config.dataPointIndex]);
                        if (event) {
                            event.stopPropagation();
                        }
                    }
                },
            },
            dataLabels: {
                style: {
                    fontSize: '12px',
                    fontFamily: 'Roboto',
                    fontWeight: 400,
                    colors: colors
                },
                background: {
                    enabled: true,
                    dropShadow: {
                        enabled:false
                    },
                    foreColor: '#fff',
                    borderWidth: 0,
                    opacity: 0.6
                },
                dropShadow: {
                    enabled: false
                }
            },
            stroke: {
                show: false
            },
            series: series,
            colors: colors,
            labels: labels
        };
    }

    private _piePieceClicked(status: number): void {
        const event = new CustomEvent("piece-clicked", {
            detail: {
                status: status
            },
            bubbles: true
        });
        this._element.dispatchEvent(event)
    }
}