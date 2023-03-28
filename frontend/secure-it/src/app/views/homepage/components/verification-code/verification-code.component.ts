import {Component, HostListener} from '@angular/core';

@Component({
  selector: 'app-verification-code',
  templateUrl: './verification-code.component.html',
  styleUrls: ['./verification-code.component.css']
})
export class VerificationCodeComponent {
  @HostListener('keyup', ['$event.target'])
  goToNextInput(target: HTMLInputElement): void {
    const event = window.event as KeyboardEvent;
    const key = event.which;
    let sib = target.nextElementSibling as HTMLInputElement;

    if (key != 9 && (key < 48 || key > 57)) {
      event.preventDefault();
      return;
    }
    if (key === 9) {
      return;
    }
    if (!sib) {
      const inputs = document.querySelectorAll('input[tabindex]');
      sib = inputs[0] as HTMLInputElement;
    }
    sib.select();
    sib.focus();
  }

  @HostListener('keydown', ['$event'])
  onKeyDown(event: KeyboardEvent): void {
    const key = event.which;
    if (key === 9 || (key >= 48 && key <= 57)) {
      return;
    }
    event.preventDefault();
  }

  @HostListener('click', ['$event.target'])
  onFocus(target: HTMLInputElement): void {
    target.select();
  }
}
