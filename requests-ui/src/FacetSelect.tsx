import React from 'react';
import Input from '@material-ui/core/Input';
import MenuItem from '@material-ui/core/MenuItem';
import ListItemText from '@material-ui/core/ListItemText';
import Select from '@material-ui/core/Select';
import Checkbox from '@material-ui/core/Checkbox';

interface Facet {
  id: string,
  name: string,
  numMatches: number
}

interface FacetProps {
  facets: Facet[]    
}

export default function FacetSelect(props: FacetProps) {
  return (
    <Select
      multiple
      value={props.facets}
      input={<Input />}
    >
      {props.facets.map((facet) => (
        <MenuItem key={facet.name} value={facet.id}>
          <Checkbox checked={false} />
          <ListItemText primary={facet.name} />
        </MenuItem>
      ))}
    </Select>
  );
}
