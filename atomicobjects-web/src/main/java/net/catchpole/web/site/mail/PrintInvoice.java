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

import net.catchpole.web.site.model.cart.Invoice;
import net.catchpole.web.site.model.cart.Item;
import net.catchpole.web.site.model.cart.Option;
import net.catchpole.web.site.model.cart.Purchase;

import java.io.PrintStream;

public class PrintInvoice {
    private Invoice invoice;

    public PrintInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public void print(PrintStream ps) {
        String format = "%-10s%-24s%-20s%10s%5s\n";
        String line = "_____________________________________________________________________";

        ps.printf(format,"SKU","Item","Options","Price","Qty");
        ps.println(line);

        for (Purchase purchase : invoice) {
            Item item = purchase.getItem();

            StringBuffer optionsString = new StringBuffer();
            for (Option option : purchase.getOptions()) {
                optionsString.append(option.getValue());
                optionsString.append(' ');
            }

            ps.printf(format, purchase.getOptionSKU(), item.getName(),
                    optionsString.toString().trim(),
                    currency(purchase.getPrice()),
                    purchase.getQuantity());
        }

        ps.printf(format,"","","","Sub Total","");
        ps.printf(format,"","","",currency(invoice.getSubTotal()),"AUD");

        Purchase shipping = invoice.getShipping();
        ps.printf(format, shipping.getOptionSKU(), shipping.getItem().getName(),
                "",
                currency(shipping.getItem().getPrice()),
                "AUD");

        ps.println(line);
        ps.printf(format,"","","","Total","");
        ps.printf(format, "", "", "", currency(invoice.getGrandTotal()), "AUD");

    }

    private String currency(int value) {
        return "$" + String.format("%8.2f", ((float)value)/100);
    }
}
