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

import {autoinject} from "aurelia-framework";
import {bindable} from "aurelia-templating";
import './test-duration-card.scss';
import {bindingMode} from "aurelia-binding";
import {DateFormatValueConverter} from "t-systems-aurelia-components/src/value-converters/date-format-value-converter";

@autoinject
export class TestDurationCard {
    @bindable start: number;
    @bindable end: number;
    @bindable({bindingMode: bindingMode.toView}) class:string;
    private _duration:number;
    private _hasEnded = false;
    private _startTooltip:string;
    private _endTooltip:string;

    startChanged() {
        this._updateDuration();
        this._startTooltip = DateFormatValueConverter.format(this.start);
    }

    endChanged() {
        this._updateDuration();
        this._endTooltip = DateFormatValueConverter.format(this.end);
    }

    private _updateDuration() {
        if (!this.end) {
            this._hasEnded = false;
            this.end = new Date().getMilliseconds();
        } else {
            this._hasEnded = true;
        }
        this._duration = this.end-this.start;
    }
}
