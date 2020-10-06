import React from 'react';
import Grid from '@material-ui/core/Grid';
import SearchIcon from '@material-ui/icons/Search';
import TextField from '@material-ui/core/TextField';
import FacetSelect from './FacetSelect';
import SearchResults from './SearchResults';
import useTrackService from './useTrackSearch';

export default function BasicTextFields() {
  const { searchQuery, setSearchQuery, results } = useTrackService();

  const searchTextChangedTo = (searchText: string) => {
    setSearchQuery(searchText);
  }

  return (
    <div>
      <Grid container spacing={1} alignItems="flex-end">
        <Grid item>
          <SearchIcon />
        </Grid>
        <Grid item>
          <TextField id="search" label="Search tracks" value={searchQuery} onChange={(e) => searchTextChangedTo(e.target.value)} />
        </Grid>
        <Grid item>
          <FacetSelect
            facets={results.artistFacets}
            label="Artists"
          />
        </Grid>
        <Grid item>
          <FacetSelect
            facets={results.albumFacets}
            label="Albums"
            onChange={(selectedFacets) => console.log(selectedFacets)}
          />
        </Grid>
      </Grid>
      <SearchResults results={results} />
    </div>
  );
}
