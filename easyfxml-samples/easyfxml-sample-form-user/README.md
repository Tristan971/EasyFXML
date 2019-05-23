# EasyFXML - Sample - User identity form
###### This presentation assumes prior knowledge of the general MVC model of [EasyFXML](../../easyfxml). See [Hello World](../easyfxml-sample-hello-world) for a refresher.

This project demonstrates a simple use-case for a form using [FXKit](../../easyfxml-fxkit)'s
Form abstraction.

## Presentation

Let's build a user submission form with per-field validation:

![User form sample application screenshot](doc/sample-form-user.png)


### Individual form field components:
Every individual form field component has a controller implementing [`FormFieldController`](../../easyfxml-fxkit/src/main/java/moe/tristan/easyfxml/fxkit/form/FormFieldController.java).

#### `String` fields (i.e.: [`StringFormFieldController`](../../easyfxml-fxkit/src/main/java/moe/tristan/easyfxml/fxkit/form/defaults/StringFormFieldController.java))

Since we more often than not deal with simple `String`-typed user inputs, it felt natural to offer at least some additional convenience for these.

```java
@Component
public class LastnameController extends StringFormFieldController {

    static final String ERROR_EMPTY_PROVIDED = "You must provide a last name";

    public TextField lastNameField;
    public Label invalidLabel;

    @Override
    public boolean validate(String fieldValue) { // Executes validation and returns true
        if (isNullOrBlank()) {                   // if the field's value was valid.
            onInvalid(ERROR_EMPTY_PROVIDED);     // Optionally triggering other actions
            return false;                        // on either case (UI feedback for example)
        } else {
            onValid();
            return true;
        }
    }
    
    /* Optional overrides to the behavior of #onValid and #onInvalid(String reason) */

    @Override
    public ObservableValue<String> getObservableValue() { // when subscribing to a field at the form 
        return lastNameField.textProperty();              // level, this is used to keep the upstream 
    }                                                     // controller up-to-date with the lastest
                                                          // user input received
                                                
    @Override
    public String getFieldName() {                        // used to denote under what name to push
        return LastnameComponent.LAST_NAME_FIELD_NAME;    // updates to value on the parent form
    }                                                     // controller side

}
```

#### Main view form component:
The main component's controller, a [`FormController`](../../easyfxml-fxkit/src/main/java/moe/tristan/easyfxml/fxkit/form/FormController.java) is
is implemented by [`UserFormController`](src/main/java/moe/tristan/easyfxml/samples/form/user/view/userform/UserFormController.java).

```java
@Component
public class UserFormController extends FormController {

    private final EasyFxml easyFxml;
    private final UserCreationService userCreationService;

    private final FirstnameComponent firstnameComponent;
    private final LastnameComponent lastnameComponent;
    private final BirthdayComponent birthdayComponent;
    private final EmailComponent emailComponent;

    public Label titleLabel;
    public VBox fieldsBox;
    public Button submitButton;

    public UserFormController(/*...*/) { /* dependency injection etc. */ }

    @Override
    public void initialize() {
        setOnClick(submitButton, this::submit);

        Stream.of(firstnameComponent, lastnameComponent, birthdayComponent, emailComponent)
              .map(field -> easyFxml.loadNode(field, VBox.class, FormFieldController.class)) // load all fields
              .forEach(load -> load
                  .afterControllerLoaded(this::subscribeToField) // add a subscription to the field in the parent form
                  .afterNodeLoaded(fieldsBox.getChildren()::add) // add the field wherever appropriate in the view, here a VBox
              );
    }

    @Override
    public void submit() {
        final List<FormFieldController> invalidFields = findInvalidFields(); // checks that all subscribed fields hold a validated value
        if (!invalidFields.isEmpty()) {
            List<String> invalidFieldNames = invalidFields.stream().map(FormFieldController::getFieldName).collect(Collectors.toList());

            displayExceptionPane(
                "Invalid fields",
                "Some fields were not valid: " + invalidFieldNames.toString(),
                new IllegalStateException("Some fields were not valid: " + invalidFieldNames.toString())
            );

            return;
        }

        UserForm userForm = ImmutableUserForm
            .builder()
            .firstName(getField(FIRST_NAME_FIELD_NAME)) // String-based field value referencing, with no type-checking
            .lastName(getField(LAST_NAME_FIELD_NAME))   // otherwise it would require a lot more boilerplate that is not
            .birthdate(getField(BIRTHDATE_FIELD_NAME))  // realistically necessary here
            .emailAddress(getField(EMAIL_FIELD_NAME))
            .build();

        userCreationService.submitUserForm(userForm); // and here, on submission, if all fields hold values as expected
                                                      // we submit the complete form model object to the appropriate service
    }

}
```

Feel free to clone and play around with the sample project to explore its possibilities!
