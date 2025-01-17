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
import {bindingMode} from "aurelia-binding";
import {data} from "../../services/report-model";
import ILogMessage = data.ILogMessage;
import {StatusConverter} from "../../services/status-converter";

interface LogMessage extends ILogMessage {
    index:number,
    cause:string;
}

@autoinject()
export class LogView {

    constructor(
        private _statusConverter:StatusConverter,
    ) {
    }

    @bindable({bindingMode: bindingMode.toView})
    logMessages:LogMessage[];

    private _filteredLogMessages:LogMessage[];

    @bindable({bindingMode: bindingMode.toView})
    showThreads;

    private _showThreads = false;

    @bindable({bindingMode: bindingMode.toView})
    search:RegExp;

    private _toggleCause(logMessage:LogMessage) {
        if (logMessage.cause) {
            logMessage.cause = null;
        } else {
            this._open(logMessage);
        }
    }

    private _open(logMessage:LogMessage) {
        let msg = "";
        logMessage.stackTrace.forEach(cause => {
            if (msg.length > 0) {
                msg += "<br>"
            }
            msg += (cause.message?.length>0?cause.message + "<br>":'') + cause.stackTraceElements.join("<br>");
        });
        logMessage.cause = msg;
    }

    private _filter() {
        if (this.search) {
            this._filteredLogMessages = this.logMessages.filter(logMessage => {
                const foundInMessage = logMessage.message.match(this.search);
                const foundInStackTrace = logMessage.stackTrace.flatMap(stackTrace => stackTrace.stackTraceElements).filter(line => line.match(this.search));

                if (foundInStackTrace.length>0) {
                    this._open(logMessage);
                }

                return foundInMessage || foundInStackTrace.length>0;
            });
        } else {
            this._filteredLogMessages = this.logMessages;
        }
    }

    bind() {
        this._filter();
    }

    logMessagesChanged() {
        this._filter();
    }

    searchChanged() {
        this._filter();
    }

    showThreadsChanged() {
        this._showThreads = this.showThreads.toLowerCase() === "true";
    }
}
