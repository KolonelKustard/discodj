import React from 'react';
import Grid from '@material-ui/core/Grid';
import SearchIcon from '@material-ui/icons/Search';
import TextField from '@material-ui/core/TextField';
import FacetSelect from './FacetSelect';
import SearchResults from './SearchResults';
import useTrackService from './useTrackSearch';

export default function BasicTextFields() {
  const { searchText, setSearchText, setSearchArtistFacet, setSearchAlbumFacet, results } = useTrackService();

  const searchTextChangedTo = (searchText: string) => {
    setSearchText(searchText);
  }

  return (
    <div>
      <Grid container spacing={1} alignItems="flex-end">
        <Grid item>
          <SearchIcon />
        </Grid>
        <Grid item>
          <TextField id="search" label="Search tracks" value={searchText} onChange={(e) => searchTextChangedTo(e.target.value)} />
        </Grid>
        <Grid item>
          <FacetSelect
            facets={results.artistFacets}
            label="Artists"
            onChange={(selectedFacet) => setSearchArtistFacet(selectedFacet)}
          />
        </Grid>
        <Grid item>
          <FacetSelect
            facets={results.albumFacets}
            label="Albums"
            onChange={(selectedFacet) => setSearchAlbumFacet(selectedFacet)}
          />
        </Grid>
      </Grid>
      <SearchResults results={results} />
    </div>
  );
}