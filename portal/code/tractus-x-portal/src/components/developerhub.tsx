// Copyright (c) 2021 Microsoft
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

import * as React from 'react';
import { observer } from 'mobx-react';

@observer
export default class DeveloperHub extends React.Component {

  public render() {
    return (
      <div className='w100pc h100pc df fdc'>
        <div className='ml50 mr50 mt50 bgfe w100-100 df fdc'>
          <div className='df fdc aic'>
            <img className='mt100' src={`${process.env.PUBLIC_URL}/semantics/ontology.png`} width='600' alt='Coming Soon' />
            <span className='fs18 w600 mt40 mb50'>The Skill Gym(nasium) will include an integrated Ontology & Query Designer as well as give access to the Catena-X Knowledge Agent SDK.</span>
          </div>
        </div>
      </div>
    );
  }
}
