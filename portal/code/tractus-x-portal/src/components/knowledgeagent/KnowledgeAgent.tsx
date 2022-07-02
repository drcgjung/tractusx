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
import { getQuery, skills, unknownSkill } from './data';
import { SparqlResult, Skill, Utterance } from './interfaces';

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
  voice:string,
  audio:HTMLAudioElement
}

const dropdownStyles: Partial<IDropdownStyles> = {
      label: { marginLeft: 20 },
      dropdown: { width: 200, marginLeft:20 },
};

const blip: HTMLAudioElement = new Audio(process.env.PUBLIC_URL+'/semantics/recording.mp3');

const helgaLaugh: HTMLAudioElement = new Audio(process.env.PUBLIC_URL+'/semantics/helga.wav');

const tinaHmm : HTMLAudioElement = new Audio(process.env.PUBLIC_URL+'/semantics/tina.wav');

const restHmm : HTMLAudioElement = new Audio(process.env.PUBLIC_URL+'/semantics/hmm.wav');

const tina:Agent = {
   nick:"tina",
   name:"Hi Tina (T-Systems)",
   voice:"Google UK English Female",
   audio:tinaHmm
 };


const helga:Agent = {
    nick:"helga",
    name:"Hi Helga (Hewlett Packard)",
    voice:"Anna",
    audio:helgaLaugh
};

const stefan:Agent = {
    nick:"stefan",
    name:"Hi Stefan (SAP)",
    voice:"Google UK English Male",
    audio:restHmm
};

const cortana:Agent = {
    nick:"cortana",
    name:"Hi Cortana (Microsoft)",
    voice:"Google UK English Male",
    audio:restHmm
};

const agents = {
 "tina": tina,
 "helga": helga,
 "stefan": stefan,
 "cortana": cortana
};

const agentOptions: IDropdownOption[] = [
  { key:tina.nick , text: tina.name},
  { key:helga.nick , text: helga.name},
  { key:stefan.nick , text: stefan.name},
  { key:cortana.nick , text: cortana.name}
];

const skillOptions: IDropdownOption[] = skills.map(aSkill =>  { return { key:aSkill.nick , text: aSkill.name}; });

const DEFAULT_PAGE_SIZE = 10;
const DEFAULT_PAGE = 0;

const filterStyles = {minHeight: '60px'};

function KnowledgeAgent(){
  const [data, setData] = useState<SparqlResult>({head:{vars:[]},results:{bindings:[]},currentPage:0,totalPages:0});
  const [filterParams, setFilterParams] = useState(new URLSearchParams(`page=${DEFAULT_PAGE}&pageSize=${DEFAULT_PAGE_SIZE}`))
  const [error, setError] = useState<[]>();
  const [selectedPageSize, setSelectedPageSize] = useState<number>(DEFAULT_PAGE_SIZE);
  const [text, setText] = useState<string>("Ask me!");
  const [agent, setAgent] = useState<Agent>(agents.cortana);
  const [skill, setSkill] = useState<Skill>(unknownSkill);

  const voices = window.speechSynthesis.getVoices();

  const speakData = function(utterance:Utterance) {
      var index=0;

      const catenaVoice = voices.filter(function (voice) {
        return voice.name == agent.voice;
      })[0];

      const waitFunction = function() {
        if(index<utterance.pauses.length) {
          setTimeout(speakFunction,utterance.pauses[index]);
        }
      };

      const speakFunction = function() {
         if(index<utterance.phrases.length) {
           var msg = new SpeechSynthesisUtterance();
           msg.voice = catenaVoice;
           msg.lang = 'en-US';
           msg.volume = 1;
           msg.pitch = 0.9;
           msg.rate = 0.9;
           msg.text = utterance.phrases[index];
           index=index+1;
           msg.onend = waitFunction;
           window.speechSynthesis.speak(msg);
         }
      };

      speakFunction();
  };

  const tellData = (res:SparqlResult) => {
    if(res!=undefined) {
     setData(res);
     if(res.results.bindings.length>0) {
        let summary=skill.summarize(res);
        if(summary!=undefined) {
            speakData(summary);
        }
     } else {
      speakData({phrases:["What the fuck?"],pauses:[0]});
     }
    } else {
      speakData({phrases:["What the fuck?"],pauses:[0]});
    }
  }

  const heyPhrase : RegExp = /^h(ey|i) ([A-Za-z]+).*$/i;

  const recognize = (event:any) => {
      blip.play();
      event.defaultPrevented=true;
      const {webkitSpeechRecognition} = window as any;
      const recognition = new webkitSpeechRecognition();
      recognition.continuous = false;
      recognition.interimResults = false;
      recognition.lang = 'en-US';

      const recognized = function( event:any ) {
        let myAgent = agent;
        for(var count=0;count<event.results.length;count++) {
          let result = event.results[count];
          for(var count2=0;count2<result.length;count2++) {
           let utterance=result[count];
           var confidence=utterance.confidence;
           var transcript=utterance.transcript;
           if(heyPhrase.test(transcript)) {
            let match=transcript.match(heyPhrase)
            let agentName=match[2].toLowerCase();
            if(agents.hasOwnProperty(agentName)) {
                myAgent=agents[agentName];
                setAgent(myAgent);
                myAgent.audio.play();
            }
           }
           if(confidence>0.8) {
            let newSkill=skills.find( aSkill => aSkill.match(transcript) != undefined);
            setSkill(newSkill);
            setText(transcript);
            /*if(newSkill.nick != unknownSkill.nick) {
                console.log("Auto Execute");
                updateQuery(newSkill,transcript);
            }*/
            return;
          }
        }
      }
      setSkill(unknownSkill);
      setText("WTF?");
      myAgent.audio.play();
      };

      recognition.onresult = recognized;

      // start listening
      recognition.start();
  };

  const onUpdateQuery = () => {
    updateQuery(skill,text);
  }

  const updateQuery = ( aSkill:Skill, aText:string ) => {
    let query=aSkill.match(aText);
    if(query!=undefined && query!="") {
        console.log("Execute "+query);
        getQuery(query)
            .then(
                res => tellData(res),
                error => setError(error.message)
            );
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
     <div className="df aife jcfs mb20" style={filterStyles}>
      <SearchBox className="w800"
                 placeholder="Go ahead. Make my day!"
                 value={text}
                 onSearch={onUpdateQuery}
                 onClear={recognize}
                 onChange={(_, newValue) => setText(newValue)}
      />
      <Dropdown placeholder="Choose an Agent"
                label="Agent"
                selectedKey={agent.nick}
                options={agentOptions}
                styles={dropdownStyles}
                onChange={(_,newValue) => { let agent = agents[newValue.key]; setAgent(agent); agent.audio.play();}}
      />
      <Dropdown placeholder="Choose a Skill"
                label="Skill"
                selectedKey={skill.nick}
                options={skillOptions}
                styles={dropdownStyles}
                onChange={(_,newValue) => setSkill(skills.find(aSkill => aSkill.nick==newValue.key))}
      />
     </div>
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
