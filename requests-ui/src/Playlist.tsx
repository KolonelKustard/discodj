import React from 'react';
import usePlaylist from './usePlaylist';

export default function Playlist() {
  const { playlist } = usePlaylist();

  return (
    <ul>
      {playlist.playlist.map(item => (
        <li key={item.id}>
          {item.title} by {item.artist}
        </li>
      ))}
    </ul>
  );
}
