import React, { useState, useEffect } from 'react';
import Grid from '@material-ui/core/Grid';
import SearchIcon from '@material-ui/icons/Search';
import TextField from '@material-ui/core/TextField';

interface Result {
  title: string
}

export default function BasicTextFields() {
  const [error, setError] = useState(null);
  const [isLoaded, setIsLoaded] = useState(false);
  const [results, setResults] = useState<Result[]>([]);

  useEffect(() => { fetch("/search")
    .then(res => res.json())
    .then((result) => {
        setIsLoaded(true);
        setResults(result.results);
      },
      // Note: it's important to handle errors here
      // instead of a catch() block so that we don't swallow
      // exceptions from actual bugs in components.
      (error) => {
        setIsLoaded(true);
        setError(error);
      }
    )}, []);

  return (
  <div>
    <Grid container spacing={1} alignItems="flex-end">
      <Grid item>
        <SearchIcon />
      </Grid>
      <Grid item>
        <TextField id="search" label="Search tracks" />
      </Grid>
    </Grid>
    <ul>
      {results.map(item => (
        <li key={item.title}>
          {item.title}
        </li>
      ))}
    </ul>
</div>
  );
}