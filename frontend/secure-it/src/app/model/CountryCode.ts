import {registerDecorator, ValidationOptions} from 'class-validator';

export function CountryCode(validationOptions?: ValidationOptions) {
  return function (object: Object, propertyName: string | symbol) {
    registerDecorator({
      name: 'countryCode',
      target: object.constructor,
      propertyName: propertyName.toString(),
      constraints: [],
      options: validationOptions,
      validator: {
        validate(value: any) {
          const countryCodeRegExp = /^[A-Z]{2}$/i;
          return countryCodeRegExp.test(value);
        },
      },
    });
  };
}
