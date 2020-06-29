export class UtilService {
    public static dataFormatada(data: Date): string {
        return new Date(data).toLocaleString().split(' ')[0];
    }
}