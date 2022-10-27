import {ShowRating} from "../../model/api/showRating";
import axios from 'axios';
import { Component, createSignal } from "solid-js";

const TopTen: Component= () => {
  const [textStore,setText] = createSignal<string>(); 
  type GetShowResponse = {
    shows: ShowRating[];
  };
  async function getTopTen(){
    try {
      const {data,status}= await axios.get<ShowRating[]>(
        'api/getTopTen',
        {
          headers: {
            Accept: 'application/json',
          },
        },
      );
    
    
    console.log(data);
    // 👇️ "response status is: 200"
    getString(data);
    }catch (error) {
      if (axios.isAxiosError(error)) {
        console.log('error message: ', error.message);
        return error.message;
      } else {
        console.log('unexpected error: ', error);
        return 'An unexpected error occurred';
      }
    }
    console.log(textStore());
  }
  function getString(data : ShowRating[]){
    var output:string="SHOW_NAME;RATING\n";
    if (data?.length !== 0){
      for(let i=0;i<data.length;i++){
        output=output.concat(data[i].showName+";"+data[i].rating+"\n");
      }
    }
    console.log(output);
    setText(output);

  }

  function download(){
    const a = document.createElement('a') // Create "a" element
    const blob = new Blob([textStore() as string], {type: "text/plain"}) // Create a blob (file-like object)
    const url = URL.createObjectURL(blob) // Create an object URL from blob
    a.setAttribute('href', url) // Set "a" element link
    a.setAttribute('download', "topten") // Set download filename
    a.click() // Start downloading
  }
  return (
    <div>

        <button onClick={getTopTen}> Get topten!</button>
        <button onClick={download} >Download topten</button>
    </div>
  )


}
export default TopTen;
