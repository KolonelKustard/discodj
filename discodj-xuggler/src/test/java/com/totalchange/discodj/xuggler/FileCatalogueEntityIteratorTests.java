package com.totalchange.discodj.xuggler;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class FileCatalogueEntityIteratorTests {
    @Test
    public void iteratesOverNestedFileList() throws IOException {
        F root = new F("music", new F[] {
            new F("dolly", new F[] {
                new F("hello", new F[] {
                    new F("dumb.mp3"),
                    new F("wasted.mp3")
                })
            }),
            new F("bob", new F[] {
                new F("changin", new F[] {
                    new F("ballad.mp3"),
                    new F("readme.txt"),
                    new F("pawn.mp3"),
                    new F("hattie.mp3")
                }),
                new F("blonde", new F[] {
                    new F("want.mp3"),
                    new F("achilles.mp3")
                })
            })
        });

        FileCatalogueEntityIterator it = new FileCatalogueEntityIterator(
                root.build(), ".mp3", ".mp4");

        assertEquals("/music/bob/blonde/achilles.mp3", it.next().getId());
        assertEquals("/music/bob/blonde/want.mp3", it.next().getId());
        assertEquals("/music/bob/changin/ballad.mp3", it.next().getId());
        assertEquals("/music/bob/changin/hattie.mp3", it.next().getId());
        assertEquals("/music/bob/changin/pawn.mp3", it.next().getId());
        assertEquals("/music/dolly/hello/dumb.mp3", it.next().getId());
        assertEquals("/music/dolly/hello/wasted.mp3", it.next().getId());
    }

    private class F {
        private boolean directory;
        private String name;
        private F[] children;

        private F(String name) {
            this.directory = false;
            this.name = name;
            this.children = new F[] {};
        }

        private F(String name, F[] children) {
            this.directory = true;
            this.name = name;
            this.children = children;
        }

        private File makeFile(F f, String parent) throws IOException {
            File file = mock(File.class);
            when(file.getName()).thenReturn(f.name);
            when(file.getCanonicalPath()).thenReturn(parent + f.name);
            when(file.isDirectory()).thenReturn(f.directory);
            when(file.isFile()).thenReturn(!f.directory);

            File[] children = new File[f.children.length];
            for (int num = 0; num < f.children.length; num++) {
                children[num] = makeFile(f.children[num], parent + f.name
                        + "/");
            }
            when(file.listFiles()).thenReturn(children);

            return file;
        }

        private File build() throws IOException {
            return makeFile(this, "/");
        }
    }
}
