package net.catchpole.web.site.mail;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import net.catchpole.web.site.model.cart.*;
import org.junit.Test;

import java.util.Iterator;

public class PrintInvoiceTest {
    @Test
    public void testPrintInvoice() {
        Catalog catalog = new Catalog();
        {
            Item i = new Item("SHIRT", "Shirts", "Look, a shirt", 8000,
                    "Put it on, fool.");
            addOptions(i, "Black", "Orange", "Green");
            catalog.addItem(i);
        }
        {
            Item i = new Item("PANTS", "Pants", "Look, pants", 8000,
                    "Put them on your legs");
            addOptions(i, "Black", "Orange", "Green");
            catalog.addItem(i);
        }
        {
            Item i = new Item("HAT", "Hats", "Look, a hat", 4000,
                    "Put it on your head");
            addOptions(i, "White", "Black", "Blue");
            catalog.addItem(i);
        }


        Invoice invoice = getTestInvoice(catalog);

        invoice.removeZeroPurchases();
        invoice.calculateShipping();

        System.out.println();
        new PrintInvoice(invoice).print(System.out);

    }

    private void addOptions(Item i, String ... colours) {
        for (String colour : colours) {
            i.addOption(new Option("Colour", colour, colour.substring(0,1)));
        }
        i.addOption(new Option("Size", "Small", "S"));
        i.addOption(new Option("Size", "Medium", "M"));
        i.addOption(new Option("Size", "Large", "L"));
    }

    public static Invoice getTestInvoice(Catalog catalog) {
        for (String itemCategory : catalog.getCategories()) {
            System.out.println(itemCategory);
            for (Item item : catalog.getItems(itemCategory)) {
                System.out.println(item);
//                for (String category : item.getCategories()) {
//                    System.out.println(category);
//                    for (Option option : item.getOptions(category)) {
//                        System.out.println(option);
//                    }
//                }
            }
        }

        Item item = catalog.getItems(catalog.getCategories().iterator().next())
                .iterator().next();
        Purchase purchase1 = new Purchase(item);
        for (String category : item.getCategories()) {
            purchase1.addOption(item.getOptions(category).iterator().next());
        }

        Purchase purchase2 = new Purchase(item);
        for (String category : item.getCategories()) {
            purchase2.addOption(item.getOptions(category).iterator().next());
        }

        Invoice invoice = new Invoice();
        invoice.addPurchase(purchase1);
        invoice.addPurchase(purchase2);

        Iterator<String> i1 = catalog.getCategories().iterator();
        i1.next();
        Item i = catalog.getItems(i1.next()).iterator().next();
        Purchase purchase3 = new Purchase(i);
        for (String category : i.getCategories()) {
            purchase3.addOption(i.getOptions(category).iterator().next());
        }
        invoice.addPurchase(purchase3);

        return invoice;
    }
}
