import React from 'react';
import Autocomplete from '@material-ui/lab/Autocomplete';
import TextField from '@material-ui/core/TextField';
import { Facet } from './useTrackSearch';

interface FacetProps {
  facets: Facet[],
  label: string,
  onChange?: (selectedFacets: Facet | null) => void
}

export default function FacetSelect(props: FacetProps) {
  return (
    <Autocomplete
      options={props.facets}
      getOptionLabel={(option) => option.name}
      style={{ width: 200 }}
      renderInput={(params) => (
        <TextField
          {...params}
          variant="standard"
          label={props.label}
        />
      )}
      onChange={(e, v) => { if (props.onChange) props.onChange(v) }}
    />
  );
}