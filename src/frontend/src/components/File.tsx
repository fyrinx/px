import { Component,createSignal} from 'solid-js'
import axios from 'axios';
const File: Component = () =>{
    let fileInputRef = null;
    
    const [store, setStore] = createSignal<File>();
      
    function handleChange(e: Event) {
        let res=e.target as HTMLInputElement;
        if (!res.files) return;
        setStore(res.files[0]);
        
        
    }
    function handleSubmit(e: Event){
        e.preventDefault();
        let reader=new FileReader();
        reader.readAsText(store() as File);
        var v: String = "";
        console.log(store() as File);
        reader.onload= function(){
            v=reader.result as String;
            const arr:String[]=v.split(/\r?\n/);
            arr.push("kek");
            sendData(arr);
            console.log(arr[1]);
        };
        
        
        
    }
    async function sendData(arr: String[]){
        try {
            const res =await axios.post<String>(
                '/api/insert',
                JSON.stringify(arr),
                {headers:{
                    'Content-Type': 'application/json',
                    Accept: 'application/json', 
                },
            },
            );
        console.log(res);
        return res;
        } catch (err) {
            if (axios.isAxiosError(err)) {
                console.log('error message: ', err.message);
                // 👇️ error: AxiosError<any, any>
                return err.message;
              } else {
                console.log('unexpected error: ', err);
                return 'An unexpected error occurred';
              }
        }
    }
    return (
    <div>
        <form>
          Upload file here.
          <input 
            id='input'
            type="file"
            accept=".txt"
            onChange={handleChange}
            />
          <button onClick={handleSubmit} type="submit" >Upload</button>
        </form>
    </div>
    )
}
export default File;
