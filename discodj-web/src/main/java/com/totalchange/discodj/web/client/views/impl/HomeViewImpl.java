/**
 * Copyright 2012 Ralph Jones.
 * 
 * This file is part of Jizz.  Jizz is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * 
 * Jizz is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Jizz.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.totalchange.discodj.web.client.views.impl;

import javax.inject.Singleton;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.totalchange.discodj.web.client.views.HomeView;

@Singleton
public class HomeViewImpl extends Composite implements HomeView {
    interface HomeViewUiBinder extends UiBinder<Widget, HomeViewImpl> {
    }

    private static HomeViewUiBinder uiBinder = GWT
            .create(HomeViewUiBinder.class);

    private Presenter presenter;

    public HomeViewImpl() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void render() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLibraryLocation(String library) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setReIndexStatus(int progress, String status) {
        // TODO Auto-generated method stub
        
    }
}
