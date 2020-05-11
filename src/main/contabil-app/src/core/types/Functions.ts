export interface Void{() : void};
export interface Action<T>{(arg  :T) : void};
export interface Func<T,R>{(arg : T) : R};
// Use this when you need pass a Component Function State to be update by another component
export type LinkedState<T = any> = Action<Action<T>>;