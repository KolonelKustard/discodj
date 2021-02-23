import React from 'react';
import { Results } from './useTrackSearch';

interface SearchResultsProps {
  results: Results
}

export default function SearchResults(props: SearchResultsProps) {
  return (
    <ul>
      {props.results.results.map(item => (
        <li key={item.id}>
          {item.title} by {item.artist}
        </li>
      ))}
    </ul>
  );
}
