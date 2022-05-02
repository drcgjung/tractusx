// Copyright (c) 2021 T-Systems
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import { IContextualMenuItem, SearchBox, Dropdown, IDropdownOption, IDropdownStyles } from '@fluentui/react';
import * as React from 'react';
import { useState } from 'react';
import { Link } from 'react-router-dom';
import ErrorMessage from '../ErrorMessage';
import DescriptionList from '../lists/DescriptionList';
import Loading from '../loading';
import HelpContextMenu from '../navigation/HelpContextMenu/HelpContextMenu';
import ListCountSelector from '../navigation/ListCountSelector';
import Pagination from '../navigation/Pagination';
import { getQuery } from './data';
import { SparqlResult} from './interfaces';

const helpMenuItems: IContextualMenuItem[] = [
      {
        key: 'howto',
        text: 'How to',
        href: 'https://confluence.catena-x.net/x/A2sAAQ',
        target: '_blank',
      },
      {
        key: 'bestpractice',
        text: 'Best Practices',
        href: 'https://confluence.catena-x.net/x/_GoAAQ',
        target: '_blank',
      },
      {
        key: 'faq',
        text: 'FAQ',
        href: 'https://confluence.catena-x.net/x/_2oAAQ',
        target: '_blank',
      },
      {
        key: 'govprocess',
        text: 'Governance Process',
        href: 'https://confluence.catena-x.net/x/AWsAAQ',
        target: '_blank',
      },
];

interface Agent {
  nick:string,
  name:string,
  voice:string
}

const dropdownStyles: Partial<IDropdownStyles> = {
      dropdown: { width: 200, marginRight: 20 },
};

const tina:Agent = {
   nick:"tina",
   name:"Hey Tina (T-Systems)",
   voice:"Google UK English Female"
 };

const birgit:Agent = {
    nick:"birgit",
    name:"Hey Birgit (Bosch)",
    voice:"Anna"
};

const stefan:Agent = {
    nick:"stefan",
    name:"Hey Stefan (SAP)",
    voice:"Google UK English Male"
};

const agents = {
 "tina": tina,
 "birgit": birgit,
 "stefan": stefan
};

const agentOptions: IDropdownOption[] = [
  { key:tina.nick , text: tina.name},
  { key:birgit.nick , text: birgit.name},
  { key:stefan.nick , text: stefan.name}
];

const DEFAULT_PAGE_SIZE = 10;
const DEFAULT_PAGE = 0;

function KnowledgeAgent(){
  const [data, setData] = useState<SparqlResult>({head:{vars:[]},results:{bindings:[]},currentPage:0,totalPages:0});
  const [filterParams, setFilterParams] = useState(new URLSearchParams(`page=${DEFAULT_PAGE}&pageSize=${DEFAULT_PAGE_SIZE}`))
  const [error, setError] = useState<[]>();
  const [selectedPageSize, setSelectedPageSize] = useState<number>(DEFAULT_PAGE_SIZE);
  const [text, setText] = useState<string>("Ask me!");
  const [agent, setAgent] = useState<Agent>(agents.birgit);
  //Hey Catena, how much glue is in vehicle model a?");

  React.useEffect(()=>{
    updateQuery();
  }, [filterParams]);

  const voices = window.speechSynthesis.getVoices();

  const speakData = function(texts:string[], pauses:number[]) {
      var index=0;

      const catenaVoice = voices.filter(function (voice) {
        return voice.name == agent.voice;
      })[0];

      const waitFunction = function() {
        if(index<pauses.length) {
          setTimeout(speakFunction,pauses[index]);
        }
      };

      const speakFunction = function() {
         if(index<texts.length) {
           var msg = new SpeechSynthesisUtterance();
           msg.voice = catenaVoice;
           msg.lang = 'en-US';
           msg.volume = 1;
           msg.pitch = 0.9;
           msg.rate = 0.9;
           msg.text = texts[index];
           index=index+1;
           msg.onend = waitFunction;
           window.speechSynthesis.speak(msg);
         }
      };

      speakFunction();
  };

  const tellData = (res:SparqlResult) => {
    if(res.head.vars.find(element => element=="vehicleType")) {
        var material = res.results.bindings[0]["material"]["value"];
        var model = res.results.bindings[0]["vehicleType"]["value"];
        var weight=0.0;
        res.results.bindings.forEach( binding => { weight=weight+Number(binding["total_weight"]["value"].replace("e0","")); });
        weight=weight/res.results.bindings.length;

        var texts=[`Hey Schorsch`, `There are`, `on average`,`${weight} kg`, `of`, `${material}`, `in`,`${model}`];
        var pauses=[0,0,0,0,0,0,0,0];

        speakData(texts,pauses);
    } else {
        var material = res.results.bindings[0]["material"]["value"];
        var texts=[`Hey Schorsch`, `${material}`, `is contained in`,res.results.bindings.length.toString(),"vehicles"];
        var pauses=[0,0,0,0,0];
        speakData(texts,pauses);
    }
    setData(res);
  }

  const heyPhrase : RegExp = /^h(ey|i) ([A-Za-z]+).*$/i;

  const recognize = (event:any) => {
      event.defaultPrevented=true;
      const {webkitSpeechRecognition} = window as any;
      const recognition = new webkitSpeechRecognition();
      recognition.continuous = false;
      recognition.interimResults = false;
      recognition.lang = 'en-US';

      const recognized = function( event:any ) {
        if(event.results.length>0) {
            if(event.results[0].length>0) {
                var confidence=event.results[0][0].confidence
                var transcript=event.results[0][0].transcript;
                if(heyPhrase.test(transcript)) {
                  let match=transcript.match(heyPhrase)
                  console.log(match);
                  if(agents.hasOwnProperty(match[2].toLowerCase())) {
                    console.log("Found agent "+agent);
                    setAgent(agents[match[2].toLowerCase()]);
                  }
                }
                setText(transcript);
                return;
            }
        }

        var texts=[`Hey Schorsch`, `I did not understand`, `please rephrase`];
        var pauses=[0,100,0];

        speakData(texts,pauses);
        setText("I did not understand, please rephrase!");
      };

      recognition.onresult = recognized;

      // start listening
      recognition.start();
  };

  const questionExp : RegExp = /^.*much ([A-Za-z ]+) is .* (vehicle model [A-Za-z]).*$/i;
  const question2Exp : RegExp = /^.*which contain ([A-Za-z ]+).*$/i;

  const camelize = (str:String) => {
    return str.split(" ").map( function(text:String) {
      return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
    }).join(" ");
  }

  const updateQuery = () => {
    console.log("trying to match text "+text);
    if(questionExp.test(text)) {
        let match=text.match(questionExp)
        console.log(match);
        let material=camelize(match[1]);
        let vehicleType=camelize(match[2]);
        let params= {
            "query":"material",
            "material":material,
            "vehicleType":vehicleType
        };
        getQuery(params)
            .then(
            res => tellData(res),
            error => setError(error.message)
        );
    } else if(question2Exp.test(text)) {
         let match=text.match(question2Exp)
         console.log(match);
         let material=camelize(match[1]);
         let params= {
             "query":"trace",
             "material":material,
             "vehicleType":"none"
         };
         getQuery(params)
             .then(
             res => tellData(res),
             error => setError(error.message)
         );
    } else {
      console.log("text did not match");
    }
  }

  const updateFilterParams = (params: string) => {
    setFilterParams(new URLSearchParams(params));
  }

  const onItemCountClick = (count: number) => {
    setSelectedPageSize(count);
    updateFilterParams(`page=0&pageSize=${count}`);
  }

  const onPageBefore = () => {
    updateFilterParams(`page=${data.currentPage - 1}&pageSize=${selectedPageSize}`);
  }

  const onPageNext = () => {
    updateFilterParams(`page=${data.currentPage + 1}&pageSize=${selectedPageSize}`);
  }

  return (
    <div className='p44 df fdc'>
      <Dropdown placeholder="Choose a Knowledge Agent"
                label="Knowledge Agent"
                selectedKey={agent.nick}
                options={agentOptions}
                styles={dropdownStyles}
                onChange={(_,newValue) => setAgent(agents[newValue.key])}
      />
      <SearchBox className="w600"
                 placeholder="Ask Me"
                 value={text}
                 onSearch={updateQuery}
                 onClear={recognize}
                 onChange={(_, newValue) => setText(newValue)}
      />
      <HelpContextMenu menuItems={helpMenuItems}></HelpContextMenu>
      {data ?
        <div>
          <h1 className="fs24 bold mb20">Knowledge Results</h1>
          <ListCountSelector activeCount={selectedPageSize} onCountClick={onItemCountClick}/>
          {data.results.bindings.length > 0 ?
            <div className="df fwrap mt20">
              {data.results.bindings.map((binding, index) => (
                    <div key={index} className='m5 p20 bgpanel flex40 br4 bsdatacatalog'>
                      <div className='mt20 mb30'>
                         {Object.entries(binding).map(([key, value]) => (
                            <DescriptionList title={key} description={value["value"]} />
                          ))}
                      </div>
                    </div>
               ))}
              <Pagination pageNumber={data.currentPage + 1}
                onPageBefore={onPageBefore}
                onPageNext={onPageNext}
                totalPages={data.totalPages}>
              </Pagination>
            </div> :
            <div className="df fdc aic">
              <span className="fs20">No matches found!</span>
          </div>
          }
        </div> :
      <div className="h100pc df jcc">
        {error ? <ErrorMessage error={error} /> : <Loading />}
      </div>
    }
    </div>
  );
}

export default KnowledgeAgent;
