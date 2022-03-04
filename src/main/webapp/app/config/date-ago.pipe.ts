import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateAgo'
})
export class DateAgoPipe implements PipeTransform {

  transform(d: any): string {

    const currentDate = new Date(new Date().toUTCString());
    const date = new Date(d);

    const year = currentDate.getFullYear() - date.getFullYear();
    const month = currentDate.getMonth() - date.getMonth();
    const day = currentDate.getDate() - date.getDate();
    const hour = currentDate.getHours() - date.getHours();
    const minute = currentDate.getMinutes() - date.getMinutes();
    const second = currentDate.getSeconds() - date.getSeconds();

    const createdSecond = (year * 31556926) + (month * 2629746) + (day * 86400) + (hour * 3600) + (minute * 60) + second;

    if (createdSecond >= 31556926) {
      const yearAgo = Math.floor(createdSecond / 31556926);
      return yearAgo > 1 ? `${String(yearAgo)} ans` : `${String(yearAgo)} an`;
    } else if (createdSecond >= 2629746) {
      const monthAgo = Math.floor(createdSecond / 2629746);
      return monthAgo > 1 ? `${String(monthAgo)} mois` : `${String(monthAgo)} mois`;
    } else if (createdSecond >= 86400) {
      const dayAgo = Math.floor(createdSecond / 86400);
      return dayAgo > 1 ? `${String(dayAgo)} jours` : `${String(dayAgo)} jour`;
    } else if (createdSecond >= 3600) {
      const hourAgo = Math.floor(createdSecond / 3600);
      return hourAgo > 1 ? `${String(hourAgo)} heures` : `${String(hourAgo)} heure`;
    } else if (createdSecond >= 60) {
      const minuteAgo = Math.floor(createdSecond / 60);
      return minuteAgo > 1 ? `${String(minuteAgo)} minutes` : `${String(minuteAgo)} minute`;
    } else if (createdSecond < 60) {
      return createdSecond > 1 ? `${String(createdSecond)} secondes` : `${String(createdSecond)} seconde`;
    } else {
      return "Maintenant";
    }
  }

}
