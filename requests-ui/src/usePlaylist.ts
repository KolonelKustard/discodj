import { useState, useEffect, useRef } from 'react';
import axios from 'axios';

const updatePlaylistEveryThisManyMillis = 2000;

export interface Playlist {
  playlist: PlaylistTrack[]
}

export interface PlaylistTrack {
  id: string,
  artist: string,
  title: string
}

export default function usePlaylist() {
  const [playlist, setPlaylist] = useState<Playlist>({
    playlist: []
  });
  const [error, setError] = useState(null);
  const nextUpdateDue = useRef(Date.now());

  const tick = () => {
    if (Date.now() >= nextUpdateDue.current) {
      nextUpdateDue.current = Date.now() + (updatePlaylistEveryThisManyMillis * 10);
      axios.get<Playlist>('/playlist')
        .then(
          (result) => {
            if (playlistChanged(playlist, result.data)) {
              setPlaylist(result.data);
            }
            nextUpdateDue.current = Date.now() + updatePlaylistEveryThisManyMillis;
          },
          // Note: it's important to handle errors here
          // instead of a catch() block so that we don't swallow
          // exceptions from actual bugs in components.
          (err) => {
            setError(err);
            nextUpdateDue.current = Date.now() + updatePlaylistEveryThisManyMillis;
          }
        )
    }
  };

  useEffect(() => {
    const interval = setInterval(tick, updatePlaylistEveryThisManyMillis / 4);
    return () => clearInterval(interval);
  }, [playlist, error]);

  return {
    playlist,
    error
  };
}

function playlistChanged(current: Playlist, latest: Playlist): boolean {
  if (current.playlist.length !== latest.playlist.length) {
    return true;
  }

  for (let num = 0; num < current.playlist.length; num++) {
    let c = current.playlist[num];
    let l = latest.playlist[num];

    if (c.id !== l.id) {
      return true;
    }
  }

  return false;
}
