package org.code_house.examples.multibundle.osgi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.code_house.examples.multibundle.home.BasePage;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class OsgiPage extends BasePage {

    private static final long serialVersionUID = 1L;

    @Inject @Named("blueprintBundleContext")
    private BundleContext context;

    public OsgiPage() {
        ISortableDataProvider<Bundle, String> provider = new SortableDataProvider<Bundle, String>() {

            public Iterator<? extends Bundle> iterator(long first, long count) {
                return Arrays.asList(context.getBundles()).iterator();
            }

            public long size() {
                return context.getBundles().length;
            }

            public IModel<Bundle> model(Bundle object) {
                return new BundleModel(object, context);
            }
        };

        List<IColumn<Bundle, String>> columns = new ArrayList<IColumn<Bundle,String>>();
        columns.add(new PropertyColumn<Bundle, String>(Model.of("ID"), "bundleId"));
        columns.add(new PropertyColumn<Bundle, String>(Model.of("Symbolic Name"), "symbolicName"));
        columns.add(new PropertyColumn<Bundle, String>(Model.of("Version"), "version"));
        add(new DefaultDataTable<Bundle, String>("bundles", columns, provider, 100));

        final Bundle bundle = context.getBundle();
        add(new Label("bundle", bundle.getSymbolicName()));
        add(new Label("bundleId") {
            @Override
            public <C> IConverter<C> getConverter(Class<C> type) {
                return new IConverter<C>() {
                    public C convertToObject(String value, Locale locale) {
                        return null;
                    }

                    public String convertToString(C value, Locale locale) {
                        return "" + bundle.getBundleId();
                    }
                };
            }
        });
    }

}
