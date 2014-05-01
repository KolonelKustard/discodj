package com.totalchange.discodj.populator;

import java.util.Iterator;

import com.totalchange.discodj.catalogue.Catalogue;

class IteratorComparator {
    ActionsToTake compare(Iterator<Catalogue.CatalogueEntity> src,
            Iterator<Catalogue.CatalogueEntity> dest) {
        ActionsToTake actionsToTake = new ActionsToTake();

<<<<<<< HEAD
        if (notYetAtEnd(src, dest, actionsToTake)) {
            compare(src, dest, src.next(), dest.next(), actionsToTake);
=======
        if (!src.hasNext()) {
            while (dest.hasNext()) {
                actionsToTake.delete(dest.next().getId());
            }
        } else if (!dest.hasNext()) {
            addRemainderForAdd(src, actionsToTake);
        } else {
            compare(src, dest, actionsToTake);
>>>>>>> branch 'master' of https://github.com/KolonelKustard/discodj.git
        }

        return actionsToTake;
    }

<<<<<<< HEAD
    private boolean notYetAtEnd(Iterator<Catalogue.CatalogueEntity> src,
            Iterator<Catalogue.CatalogueEntity> dest,
            ActionsToTake actionsToTake) {
        if (!src.hasNext()) {
            while (dest.hasNext()) {
                actionsToTake.delete(dest.next().getId());
            }
            return false;
        } else if (!dest.hasNext()) {
            while (src.hasNext()) {
                actionsToTake.add(src.next().getId());
            }
            return false;
        } else {
            return true;
        }
    }

    private void compare(Iterator<Catalogue.CatalogueEntity> src,
            Iterator<Catalogue.CatalogueEntity> dest,
            Catalogue.CatalogueEntity currSrc,
            Catalogue.CatalogueEntity currDest, ActionsToTake actionsToTake) {
        int compare = currSrc.getId().compareTo(currDest.getId());

        if (compare > 0) {
            actionsToTake.delete(currDest.getId());
            if (dest.hasNext()) {
                compare(src, dest, currSrc, dest.next(), actionsToTake);
            }
        } else if (compare < 0) {
            actionsToTake.add(currSrc.getId());
            if (src.hasNext()) {
                compare(src, dest, src.next(), currDest, actionsToTake);
            }
        } else if (!currSrc.getLastModified()
                .equals(currDest.getLastModified())) {
            actionsToTake.update(currSrc.getId());
        }

        if (notYetAtEnd(src, dest, actionsToTake)) {
            compare(src, dest, src.next(), dest.next(), actionsToTake);
=======
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
>>>>>>> branch 'master' of https://github.com/KolonelKustard/discodj.git
        }
    }
}
