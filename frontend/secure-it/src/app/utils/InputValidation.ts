import {AbstractControl, ValidatorFn} from "@angular/forms";

export function phoneNumberValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const numberRegex = /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/;
    const valid = numberRegex.test(control.value);
    return valid ? null : {invalidPhoneNumber: true};
  };
}

export function passwordValidator(): ValidatorFn {
  return (control: AbstractControl): { [key: string]: any } | null => {
    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^\w\s]).{12,}$/;
    const valid = passwordRegex.test(control.value);
    return valid ? null : {invalidPassword: true};
  };
}

export function countryCodeValidator(control: { value: any; }) {
  const countryCode = control.value;
  if (countryCode && countryCode.length === 2) {
    return null;
  } else {
    return {invalidCountryCode: true};
  }
}
