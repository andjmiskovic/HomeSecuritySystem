import {registerDecorator, ValidationOptions, ValidationArguments} from 'class-validator';

export function AlgorithmAndKeySize(validationOptions?: ValidationOptions) {
  return function (object: Object, propertyName: string | symbol) {
    registerDecorator({
      name: 'algorithmAndKeySize',
      target: object.constructor,
      propertyName: propertyName.toString(),
      constraints: [],
      options: validationOptions,
      validator: {
        validate(value: any, args: ValidationArguments) {
          const algorithms = ['RSA', 'DSA', 'EC'];
          const {algorithm, keySize} = value;
          if (!algorithms.includes(algorithm)) {
            return false;
          }
          switch (algorithm) {
            case 'RSA':
              return keySize >= 2048 && keySize % 256 === 0;
            case 'DSA':
              return keySize >= 2048 && keySize % 256 === 0;
            case 'EC':
              return [256, 384, 521].includes(keySize);
            default:
              return false;
          }
        },
      },
    });
  };
}
