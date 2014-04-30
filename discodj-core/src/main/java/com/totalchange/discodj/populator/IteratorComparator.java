package com.totalchange.discodj.populator;

import java.util.Iterator;

import com.totalchange.discodj.catalogue.Catalogue;

class IteratorComparator {
    ActionsToTake compare(Iterator<Catalogue.CatalogueEntity> src,
            Iterator<Catalogue.CatalogueEntity> dest) {
        ActionsToTake actionsToTake = new ActionsToTake();

        if (!src.hasNext()) {
            while (dest.hasNext()) {
                actionsToTake.delete(dest.next().getId());
            }
        } else if (!dest.hasNext()) {
            addRemainderForAdd(src, actionsToTake);
        } else {
            compare(src, dest, actionsToTake);
        }

        return actionsToTake;
    }

    private void compare(Iterator<Catalogue.CatalogueEntity> src,
            Iterator<Catalogue.CatalogueEntity> dest,
            ActionsToTake actionsToTake) {
        Catalogue.CatalogueEntity currDest = dest.next();
        while (src.hasNext()) {
            Catalogue.CatalogueEntity currSrc = src.next();

            int compare = currSrc.getId().compareTo(currDest.getId());

            while (compare > 0) {
                
            }

            if (compare < 0) {
                actionsToTake.add(currSrc.getId());
            } else {
                if (dest.hasNext()) {
                    currDest = dest.next();
                } else {
                    addRemainderForAdd(src, actionsToTake);
                }
            }
        }
    }

    private void addRemainderForAdd(Iterator<Catalogue.CatalogueEntity> src,
            ActionsToTake actionsToTake) {
        while (src.hasNext()) {
            actionsToTake.add(src.next().getId());
        }
    }
}
