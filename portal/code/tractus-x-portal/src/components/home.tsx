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
import Header from './header';
import { Nav, INavStyles, INavLinkGroup, INavLink } from '@fluentui/react/lib/Nav';
import { Redirect, Route, RouteComponentProps, Switch, withRouter } from 'react-router-dom';
import Dashboard from './dashboard';
import AppStore from './appstore';
import DataCatalog from './datacatalog';
import DeveloperHub from './developerhub';
import { ThemeProvider } from '@fluentui/react';
import NotImp from './notimplemented';
import AppDetail from './appdetail';
import OrgDetails from './orgdetails';
import UserMgmt from './usermanagement';
import MyConnectors from './myconnectors';
import MyData from './mydata';
import { observable } from 'mobx';
import NotificationCenter from './notificationcenter';
import YellowPages from './yellowpages';
import { NewSemanticModel } from './semantics/NewSemanticModel';
import SemanticHub from './semantics/SemanticHub';
import SemanticModelDetail from './semantics/SemanticModelDetail';
import DigitalTwinOverview from './digitaltwins/DigitalTwinOverview';
import { DigitalTwinDetail } from './digitaltwins/DigitalTwinDetail';
import KnowledgeAgent from './knowledgeagent/KnowledgeAgent';
import Admin from './admin';
import Help from './help';

const navStyles: Partial<INavStyles> = {
  root: {
    width: 250,
    boxSizing: 'border-box',
    border: '1px solid #eee',
    overflowY: 'auto'
  },
  // these link styles override the default truncation behavior
  link: {
    whiteSpace: 'normal',
    lineHeight: 'inherit'
  }
};

// adding an empty title string to each link removes the tooltip;
// it's unnecessary now that the text wraps, and will not truncate
const navLinkGroups: INavLinkGroup[] = [
  {
    links: [
      {
        name: 'my Apps',
        url: `${process.env.PUBLIC_URL}/home/dashboard`,
        key: 'key1',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      },
      {
        name: 'my Data',
        url: `${process.env.PUBLIC_URL}/home/mydata`,
        key: 'key2',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      },
      {
        name: 'my Connectors',
        url: `${process.env.PUBLIC_URL}/home/myconnectors`,
        key: 'key3',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      }
    ]
  }
];

const navLinkGroups2: INavLinkGroup[] = [
  {
    links: [
      {
        name: 'Notification Center',
        url: `${process.env.PUBLIC_URL}/home/notification`,
        key: 'key4',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      },
      {
        name: 'Organization',
        url: `${process.env.PUBLIC_URL}/home/organization`,
        key: 'key5',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      },
      {
        name: 'Partner Network',
        url: `${process.env.PUBLIC_URL}/home/partners`,
        key: 'key6',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      },
      {
        name: 'User Management',
        url: `${process.env.PUBLIC_URL}/home/usermanagement`,
        key: 'key7',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      }
    ]
  }
];

const navLinkGroupsData: INavLinkGroup[] = [
  {
    links: [
      {
        name: 'Browse & Search',
        url: `${process.env.PUBLIC_URL}/home/datacatalog`,
        key: 'key1',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      },
      {
        name: 'Resources',
        url: `${process.env.PUBLIC_URL}/home/datacatalog`,
        key: 'key2',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      },
      {
        name: 'Connectors',
        url: `${process.env.PUBLIC_URL}/home/datacatalog`,
        key: 'key3',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      }
    ]
  }
];

const navLinkGroupsSemantics: INavLinkGroup[] = [
  {
    links: [
      {
        name: 'Browse & Search',
        url: `${process.env.PUBLIC_URL}/home/semantichub`,
        key: 'key1',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      },
      {
        name: 'Create or Modify Model',
        url: `${process.env.PUBLIC_URL}/home/newsemanticmodel`,
        key: 'key2',
        expandAriaLabel: 'Expand section',
        collapseAriaLabel: 'Collapse section',
        title: ''
      }
    ]
  }
];

const noNav = ['knowledge', 'digitaltwin', 'developerhub', 'appstore', 'notification', 'organization', 'partners', 'usermanagement'];

@observer
class Home extends React.Component<RouteComponentProps> {
  @observable public static selectedKey1 = 'key1';
  @observable public static selectedKey2 = 'key0';

  linkClick(ev: React.MouseEvent<HTMLElement, MouseEvent>, item: INavLink): void {
    ev.stopPropagation();
    ev.preventDefault();
    Home.selectedKey1 = 'key' + (navLinkGroups[0].links.indexOf(item) + 1).toString();
    Home.selectedKey2 = 'key' + (navLinkGroups2[0].links.indexOf(item) + 4).toString();
    if (Home.selectedKey1 === 'key0' && Home.selectedKey2 === 'key3') {
      Home.selectedKey1 = 'key1';
    }

    this.props.history.push(item.url);
  }

  hasLeftTopNavi(): boolean {
    return noNav.filter(nav => window.location.href.includes(nav)).length === 0;
  }

  public render() {
    let groups = navLinkGroups;
    if (window.location.href.includes('/datacatalog')) groups = navLinkGroupsData;
    if (window.location.href.includes('/semantichub') || window.location.href.includes('/newsemanticmodel')) groups = navLinkGroupsSemantics;

    return (
      <div className='w100pc h100pc df fdc bgf5'>
        <Header href={window.location.href} />
        <div className='df w100pc flex1'>
          <ThemeProvider theme={{ palette: { themePrimary: '#E6AA1E' } }}>
            <div className='df fdc w250 h100pc'>
              {/* {this.hasLeftTopNavi() && <Nav className='bgwhite' selectedKey={Home.selectedKey1} ariaLabel='Navigation panel' styles={navStyles} groups={groups}
                onLinkClick={(ev, item) => this.linkClick(ev, item)} />} */}
              <div className='flex1 bgwhite' />
              <li className='admin_button'>
                <a className='' href="/home/admin">
                  Administration
                </a>
              </li>
              <Nav className='bgwhite' selectedKey={Home.selectedKey2} ariaLabel='Navigation panel' styles={navStyles} groups={navLinkGroups2}
                onLinkClick={(ev, item) => this.linkClick(ev, item)} />
            </div>
          </ThemeProvider>
          <div className='flex1 h100pc ova'>
            <Switch>
              <Redirect path={`${process.env.PUBLIC_URL}/home`} exact to={`${process.env.PUBLIC_URL}/home/dashboard`} />
              <Route path={`${process.env.PUBLIC_URL}/home/dashboard`} component={(props) => <Dashboard {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/apps`} component={(props) => <AppStore {...props} />} />
              {/* <Route path='/home/datacatalog' component={(props) => <DataCatalog {...props} />} /> */}
              <Route path={`${process.env.PUBLIC_URL}/home/semantichub`} component={(props) => <SemanticHub {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/newsemanticmodel`} component={(props) => <NewSemanticModel {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/semanticmodel/:id`} component={(props) => <SemanticModelDetail {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/knowledge`} component={(props) => <KnowledgeAgent {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/digitaltwins`} component={(props) => <DigitalTwinOverview {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/digitaltwin/:id`} component={(props) => <DigitalTwinDetail {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/skillgym`} component={(props) => <DeveloperHub {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/appdetail/:id`} component={(props) => <AppDetail {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/assets`} component={(props) => <MyData {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/myconnectors`} component={(props) => <MyConnectors {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/connector`} component={(props) => <MyConnectors {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/organization`} component={(props) => <OrgDetails {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/usermanagement`} component={(props) => <UserMgmt {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/partners`} component={(props) => <YellowPages {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/notification`} component={(props) => <NotificationCenter {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/notimp`} component={(props) => <NotImp {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/admin`} component={(props) => <Admin {...props} />} />
              <Route path={`${process.env.PUBLIC_URL}/home/help`} component={(props) => <Help {...props} />} />
            </Switch>
          </div>
        </div>
      </div>
    );
  }
}

export default withRouter(Home);
