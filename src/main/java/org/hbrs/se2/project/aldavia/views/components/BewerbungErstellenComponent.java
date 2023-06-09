package org.hbrs.se2.project.aldavia.views.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.hbrs.se2.project.aldavia.control.BewerbungsControl;
import org.hbrs.se2.project.aldavia.control.exception.BewerbungsException;
import org.hbrs.se2.project.aldavia.dtos.StellenanzeigeDTO;

public class BewerbungErstellenComponent extends Dialog {

    private final BewerbungsControl bewerbungsControl;

    private final String studentUsername;
    private final StellenanzeigeDTO stellenanzeigeDTO;

    private final TextArea bewerbungsTextfield = new TextArea("Bewerbungsschreiben", "Beschreibe hier warum du dich für diese Stelle bewirbst und warum du der/die Richtige für diese Stelle bist.");
    public BewerbungErstellenComponent(BewerbungsControl bewerbungsControl, StellenanzeigeDTO stellenanzeigeDTO, String studentUsername) {
        this.bewerbungsControl = bewerbungsControl;
        this.stellenanzeigeDTO = stellenanzeigeDTO;
        this.studentUsername = studentUsername;
        this.add(createDiv());
        this.open();
    }

    private Div createDiv(){
        Div div = new Div();
        div.addClassName("div");
        div.add(setUpBewerbungsLayout());
        return div;
    }


    private VerticalLayout setUpBewerbungsLayout() {
        VerticalLayout bewerbungsLayout = new VerticalLayout();
        bewerbungsLayout.addClassName("bewerbungs-layout");
        bewerbungsLayout.add(new H1("Bewerbung erstellen"));
        bewerbungsLayout.add(new Label("Mit deiner Bewerbung werden dem Unternehmen dein Profil und dein Bewerbungsschreiben übermittelt"));
        bewerbungsLayout.add(new Anchor("/profile/" + studentUsername, "Du kannst dein Profil hier einsehen und bearbeiten"));
        bewerbungsTextfield.setWidthFull();
        bewerbungsLayout.add(bewerbungsTextfield);
        bewerbungsLayout.add(setUpButtons());
        return bewerbungsLayout;
    }

    private HorizontalLayout setUpButtons(){
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.addClassName("buttons");
        buttons.add(createAbbruchButton());
        buttons.add(createBewerbungAbschickenButton());
        return buttons;
    }

    private Button createAbbruchButton(){
        Button abbruchButton = new Button("Abbrechen");
        abbruchButton.addClickListener(e -> this.close());
        return abbruchButton;
    }

    private Button createBewerbungAbschickenButton(){
        Button createButton = new Button("Bewerbung abschicken");
        createButton.addClickListener(e -> {
            if (bewerbungsTextfield.getValue().isEmpty()) {
                Notification.show("Bitte fülle das Bewerbungsschreiben aus");
            }
            else {
                addBewerbung();
                this.close();
            }
        });
        return createButton;
    }



    private void addBewerbung() {
        try {
            bewerbungsControl.addBewerbung(studentUsername, stellenanzeigeDTO, bewerbungsTextfield.getValue());
            Notification.show("Bewerbung erfolgreich abgeschickt").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (BewerbungsException e) {
            Notification.show("Du hast dich bereits auf diese Stelle beworben");
        }
    }


}
