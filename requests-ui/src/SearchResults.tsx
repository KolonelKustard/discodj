import React from 'react';

interface Result {
  id: string,
  artist: string,
  title: string
}

interface Results {
  page: number,
  numPages: number,
  results: Result[]
}

interface SearchResultsProps {
  results: Results
}

export default function SearchResults(props: SearchResultsProps) {
  return (
    <ul>
      {props.results.results.map(item => (
        <li key={item.title}>
          {item.title}
        </li>
      ))}
    </ul>
  );
}
