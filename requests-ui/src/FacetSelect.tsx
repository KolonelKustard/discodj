import React from 'react';
import Autocomplete from '@material-ui/lab/Autocomplete';
import TextField from '@material-ui/core/TextField';

interface Facet {
  id: string,
  name: string,
  numMatches: number
}

interface FacetProps {
  facets: Facet[],
  label: string
}

export default function FacetSelect(props: FacetProps) {
  return (
    <Autocomplete
      multiple
      id="tags-standard"
      options={props.facets}
      getOptionLabel={(option) => option.name}
      renderInput={(params) => (
        <TextField
          {...params}
          variant="standard"
          label={props.label}
        />
      )}
    />
  );
}
