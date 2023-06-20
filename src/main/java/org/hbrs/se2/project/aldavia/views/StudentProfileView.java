package org.hbrs.se2.project.aldavia.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.hbrs.se2.project.aldavia.control.StudentProfileControl;
import org.hbrs.se2.project.aldavia.control.exception.PersistenceException;
import org.hbrs.se2.project.aldavia.control.exception.ProfileException;
import org.hbrs.se2.project.aldavia.dtos.*;
import org.hbrs.se2.project.aldavia.util.Globals;
import org.hbrs.se2.project.aldavia.views.components.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hbrs.se2.project.aldavia.views.LoggedInStateLayout.getCurrentUserName;


@Route(value = Globals.Pages.PROFILE_VIEW, layout = LoggedInStateLayout.class)
@PageTitle("Profil")
@CssImport("./styles/views/profile/studentProfile.css")
public class StudentProfileView extends VerticalLayout implements HasUrlParameter<String> {

    private final StudentProfileControl studentProfileControl;

    private final UI ui = UI.getCurrent();

    private StudentProfileDTO studentProfileDTO;
    private Div profileWrapper = null;

    private PersonalProfileDetailsComponent studentPersonalDetailsComponent;
    private AboutStudentComponent aboutStudentComponent;
    private SkillsComponent skillsComponent;
    private LanguageComponent languageComponent;
    private QualificationComponent qualificationComponent;
    private EditAndSaveProfileButton editAndSaveProfileButton;

    @Override
    public void setParameter(BeforeEvent event,
                             String parameter) {
        try {
            studentProfileDTO = studentProfileControl.getStudentProfile(parameter);
            ui.access(() -> {

                if (profileWrapper == null) {
                    studentPersonalDetailsComponent = new PersonalProfileDetailsComponent(studentProfileDTO,studentProfileControl);
                    profileWrapper = new Div();
                    profileWrapper.addClassName("profile-wrapper");
                    profileWrapper.add(studentPersonalDetailsComponent);
                    profileWrapper.add(createBottomLayout());
                    add(profileWrapper);
                }
            });
        } catch (ProfileException e) {
            throw new RuntimeException(e);
        }
    }
    @Autowired
    public StudentProfileView(StudentProfileControl studentProfileControl){
        this.studentProfileControl = studentProfileControl;
        editAndSaveProfileButton = new EditAndSaveProfileButton();
        addClassName("profile-view");

        editAndSaveProfileButton.addListenerToEditButton(e -> {
            switchToEditMode();
        });
        editAndSaveProfileButton.addListenerToSaveButton(e -> {
            try {
                switchToViewMode();
            } catch (PersistenceException | ProfileException persistenceException) {
                persistenceException.printStackTrace();
            }
        });

        editAndSaveProfileButton.setEditButtonVisible(true);
        editAndSaveProfileButton.setSaveButtonVisible(false);

        add(editAndSaveProfileButton);

    }

    private void switchToEditMode(){
        editAndSaveProfileButton.setEditButtonVisible(false);
        editAndSaveProfileButton.setSaveButtonVisible(true);

        studentPersonalDetailsComponent.switchEditMode();
        aboutStudentComponent.switchEditMode();
        skillsComponent.switchEditMode();
        languageComponent.switchEditMode();
        qualificationComponent.switchEditMode();

    }

    private void switchToViewMode() throws PersistenceException, ProfileException {
        editAndSaveProfileButton.setEditButtonVisible(true);
        editAndSaveProfileButton.setSaveButtonVisible(false);

        studentPersonalDetailsComponent.switchViewMode(getCurrentUserName());
        aboutStudentComponent.switchViewMode(getCurrentUserName());
        skillsComponent.switchViewMode(getCurrentUserName());
        languageComponent.switchViewMode(getCurrentUserName());
        qualificationComponent.switchViewMode(getCurrentUserName());
    }

    private HorizontalLayout createBottomLayout(){
        HorizontalLayout bottomLayout = new HorizontalLayout();
        bottomLayout.addClassName("bottom");
        bottomLayout.add(createLeftLayout());
        bottomLayout.add(createQualifikationsLayout());
        return bottomLayout;
    }

    private VerticalLayout createLeftLayout(){
        aboutStudentComponent = new AboutStudentComponent(studentProfileDTO,studentProfileControl);
        skillsComponent = new SkillsComponent(studentProfileDTO,studentProfileControl);
        languageComponent = new LanguageComponent(studentProfileDTO,studentProfileControl);
        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.addClassName("left");
        leftLayout.add(aboutStudentComponent);
        leftLayout.add(skillsComponent);
        leftLayout.add(languageComponent);
        return leftLayout;
    }



    /*private HorizontalLayout createInteressenLayout(){
        HorizontalLayout interessenLayout = new HorizontalLayout();
        interessenLayout.addClassName("interessen");
        for (TaetigkeitsfeldDTO taetigkeitsfeldDTO : studentProfileDTO.getTaetigkeitsfelder()){
            interessenLayout.add(new Label(taetigkeitsfeldDTO.getName()));
        }
        return interessenLayout;
    }*/


    private VerticalLayout createQualifikationsLayout(){
        VerticalLayout qualifikationsLayout = new VerticalLayout();
        qualifikationsLayout.addClassName("qualifikationenRight");
        qualificationComponent = new QualificationComponent(studentProfileControl,studentProfileDTO);
        qualifikationsLayout.add(qualificationComponent);
        return qualifikationsLayout;
    }


}
