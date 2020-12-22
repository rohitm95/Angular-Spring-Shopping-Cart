import { AbstractControl, FormGroup } from '@angular/forms';

export class PasswordValidator {
    static mustMatch(controlName: string, matchingControlName: string) {
        return (formGroup: FormGroup) => {
            const control = formGroup.controls[controlName];
            const matchingControl = formGroup.controls[matchingControlName];

            if (matchingControl.errors && !matchingControl.errors.mustMatch) {
                // return if another validator has already found an error on the matchingControl
                return;
            }

            // set error on matchingControl if validation fails
            if (control.value !== matchingControl.value) {
                matchingControl.setErrors({ mustMatch: true });
            } else {
                matchingControl.setErrors(null);
            }

        }
    }

    static mustNotMatch(controlName: string, matchingControlName: string) {
        return (formGroup: FormGroup) => {
            const control = formGroup.controls[controlName];
            const matchingControl = formGroup.controls[matchingControlName];

            if (matchingControl.errors && !matchingControl.errors.mustNotMatch) {
                // return if another validator has already found an error on the matchingControl
                return;
            }

            // set error on matchingControl if validation fails
            if (control.value == matchingControl.value) {
                matchingControl.setErrors({ mustNotMatch: true });
            } else {
                matchingControl.setErrors(null);
            }

        }
    }

    static mustValidByOption(controlOption: string, controlValue: string) {
        return (formGroup: FormGroup) => {

            const option = formGroup.controls[controlOption];
            const optionValue = formGroup.controls[controlValue];

            const emailPattern: RegExp = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
            const mobNumberPattern = "^((\\+91-?)|0)?[0-9]{10}$"

            if (optionValue.errors && !optionValue.errors.mustValidByOption) {
                // return if another validator has already found an error on the matchingControl
                return;
            }

            // set error on matchingControl if validation fails
            if (option.value) {
                if (!optionValue.value.match(mobNumberPattern)) {
                    optionValue.setErrors({ mustValidByOption: true });
                }
            }
            else if (!option.value) {
                if (!optionValue.value.match(emailPattern)) {
                    optionValue.setErrors({ mustValidByOption: true });
                }
            }
        }
    }

    static mustBeLeser(controlName: string, matchingControlName: string) {
        return (formGroup: FormGroup) => {
            const control = formGroup.controls[controlName];
            const matchingControl = formGroup.controls[matchingControlName];

            if (matchingControl.errors && !matchingControl.errors.mustBeLeser) {
                // return if another validator has already found an error on the matchingControl
                return;
            }

            // set error on matchingControl if validation fails
            if (parseInt(control.value) > parseInt(matchingControl.value)) {
                matchingControl.setErrors({ mustBeLeser: true });
            } else {
                matchingControl.setErrors(null);
            }

        }
    }
}