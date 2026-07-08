package pe.joedayz.workshop.competition;

import ch.martinelli.demo.aitaf.db.enums.CompetitionStatus;
import ch.martinelli.demo.aitaf.db.tables.records.CompetitionRecord;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import java.time.LocalDate;

/**
 * CU-001 Gestionar Competiciones: allows administrators to create, list, edit
 * and delete competitions.
 */
@Route("competitions")
@RouteAlias("")
@PageTitle("Competiciones")
public class CompetitionView extends VerticalLayout {

    private final CompetitionRepository repository;

    private final Grid<CompetitionRecord> grid = new Grid<>(CompetitionRecord.class, false);

    private final Dialog editorDialog = new Dialog();
    private final Binder<CompetitionRecord> binder = new Binder<>(CompetitionRecord.class);
    private final TextField nameField = new TextField("Nombre");
    private final DatePicker dateField = new DatePicker("Fecha");
    private final TextField locationField = new TextField("Ubicación");
    private final Select<CompetitionStatus> statusField = new Select<>();

    private CompetitionRecord editing;

    public CompetitionView(CompetitionRepository repository) {
        this.repository = repository;
        setSizeFull();

        add(createToolbar());
        configureGrid();
        add(grid);
        configureEditor();

        refreshGrid();
    }

    private HorizontalLayout createToolbar() {
        Button newButton = new Button("Nueva competición", VaadinIcon.PLUS.create());
        newButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newButton.addClickListener(e -> {
            CompetitionRecord record = new CompetitionRecord();
            record.setStatus(CompetitionStatus.PLANNED);
            openEditor(record);
        });

        HorizontalLayout toolbar = new HorizontalLayout(newButton);
        toolbar.setWidthFull();
        return toolbar;
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.addColumn(CompetitionRecord::getName).setHeader("Nombre").setAutoWidth(true).setSortable(true);
        grid.addColumn(CompetitionRecord::getDate).setHeader("Fecha").setAutoWidth(true).setSortable(true);
        grid.addColumn(CompetitionRecord::getLocation).setHeader("Ubicación").setAutoWidth(true);
        grid.addColumn(c -> statusLabel(c.getStatus())).setHeader("Estado").setAutoWidth(true);
        grid.addComponentColumn(this::createActions).setHeader("Acciones").setAutoWidth(true).setFlexGrow(0);
    }

    private HorizontalLayout createActions(CompetitionRecord competition) {
        Button edit = new Button(VaadinIcon.EDIT.create(), e -> openEditor(competition));
        edit.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        edit.setTooltipText("Editar");

        Button delete = new Button(VaadinIcon.TRASH.create(), e -> confirmDelete(competition));
        delete.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE, ButtonVariant.LUMO_ERROR);
        delete.setTooltipText("Eliminar");

        HorizontalLayout actions = new HorizontalLayout(edit, delete);
        actions.setPadding(false);
        actions.setSpacing(false);
        return actions;
    }

    private void configureEditor() {
        statusField.setLabel("Estado");
        statusField.setItems(CompetitionStatus.values());
        statusField.setItemLabelGenerator(this::statusLabel);

        binder.forField(nameField)
                .asRequired("El nombre es obligatorio")
                .withValidator(new StringLengthValidator(
                        "El nombre debe tener entre 1 y 255 caracteres", 1, 255))
                .withValidator((value, ctx) ->
                        repository.nameExists(value, editing == null ? null : editing.getId())
                                ? ValidationResult.error("Ya existe una competición con ese nombre")
                                : ValidationResult.ok())
                .bind(CompetitionRecord::getName, CompetitionRecord::setName);

        binder.forField(dateField)
                .asRequired("La fecha es obligatoria")
                .withValidator((value, ctx) -> {
                    boolean creating = editing == null || editing.getId() == null;
                    if (creating && value != null && value.isBefore(LocalDate.now())) {
                        return ValidationResult.error("La fecha no puede ser anterior a hoy");
                    }
                    return ValidationResult.ok();
                })
                .bind(CompetitionRecord::getDate, CompetitionRecord::setDate);

        binder.forField(locationField)
                .asRequired("La ubicación es obligatoria")
                .withValidator(new StringLengthValidator(
                        "La ubicación debe tener entre 1 y 255 caracteres", 1, 255))
                .bind(CompetitionRecord::getLocation, CompetitionRecord::setLocation);

        binder.forField(statusField)
                .asRequired("El estado es obligatorio")
                .bind(CompetitionRecord::getStatus, CompetitionRecord::setStatus);

        FormLayout formLayout = new FormLayout(nameField, dateField, locationField, statusField);

        Button save = new Button("Guardar", e -> save());
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Button cancel = new Button("Cancelar", e -> editorDialog.close());

        editorDialog.add(formLayout);
        editorDialog.getFooter().add(cancel, save);
        editorDialog.setWidth("480px");
    }

    private void openEditor(CompetitionRecord competition) {
        this.editing = competition;
        binder.readBean(competition);
        editorDialog.setHeaderTitle(
                competition.getId() == null ? "Nueva competición" : "Editar competición");
        editorDialog.open();
    }

    private void save() {
        try {
            binder.writeBean(editing);
        } catch (ValidationException ex) {
            return;
        }
        repository.save(editing);
        editorDialog.close();
        refreshGrid();
        Notification notification = Notification.show("Competición guardada");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void confirmDelete(CompetitionRecord competition) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader("¿Eliminar \"" + competition.getName() + "\"?");
        String message = "Se eliminarán también sus categorías, pruebas y resultados asociados.";
        if (repository.hasResults(competition.getId())) {
            message = "Esta competición tiene resultados registrados. " + message;
        }
        dialog.setText(message);
        dialog.setCancelable(true);
        dialog.setConfirmText("Eliminar");
        dialog.setConfirmButtonTheme("error primary");
        dialog.addConfirmListener(e -> {
            repository.deleteById(competition.getId());
            refreshGrid();
            Notification notification = Notification.show("Competición eliminada");
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });
        dialog.open();
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }

    private String statusLabel(CompetitionStatus status) {
        if (status == null) {
            return "";
        }
        return switch (status) {
            case PLANNED -> "Planificada";
            case ONGOING -> "En curso";
            case COMPLETED -> "Finalizada";
        };
    }
}