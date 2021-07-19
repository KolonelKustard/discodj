import React from 'react';
import Container from '@material-ui/core/Container';
import AppBar from '@material-ui/core/AppBar';
import Box from '@material-ui/core/Box';
import Tabs from '@material-ui/core/Tabs';
import Tab from '@material-ui/core/Tab';
import Search from './Search';
import Playlist from './Playlist';

export default function BasicTextFields() {
  const [selectedTab, setSelectedTab] = React.useState(0);

  return (
    <Container>
      <AppBar position="static">
        <Tabs
          value={selectedTab}
          onChange={(event: React.ChangeEvent<{}>, newValue: number) => setSelectedTab(newValue)}
        >
          <Tab label="Search" />
          <Tab label="Playlist" />
        </Tabs>
      </AppBar>
      <div hidden={selectedTab !== 0}>
        <Box p={3}><Search /></Box>
      </div>
      {selectedTab === 1 && <Box p={3}><Playlist /></Box>}
    </Container>
  );
}
