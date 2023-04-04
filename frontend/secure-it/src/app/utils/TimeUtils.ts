export function getDateTime(dateString: string) {
  const date = new Date(dateString);

  const hours = date.getUTCHours().toString().padStart(2, '0');
  const minutes = date.getUTCMinutes().toString().padStart(2, '0');
  const day = date.getUTCDate().toString().padStart(2, '0');
  const month = (date.getUTCMonth() + 1).toString().padStart(2, '0'); // Note: months start from 0
  const year = date.getUTCFullYear().toString();

  return `${day}.${month}.${year}. ${hours}:${minutes}`;
}
